package kyr.company.cameraapi_recyclerview

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ExifInterface
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.picture_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyr.company.cameraapi_recyclerview.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnPictureMenuItemClickListener {

    private val TAG : String = "LOG"
    private val REQUEST_CAMERA_PERMISSION : Int = 100

    //카메라 후면 뒷면 설정하는 변수
    private lateinit var cameraId : String
    //카메라 장치
    var cameraDevice: CameraDevice? = null
    
    //카메라 버퍼 사이즈 조정 변수
    private lateinit var imageDimension : Size
    
    //카메라 세션 및 요청 변수
    var cameraCaptureSessions: CameraCaptureSession? = null
    var captureRequestBuilder: CaptureRequest.Builder? = null
    
    //카메라 이미지 관련 변수
    private var imageReader: ImageReader? = null

    //카메라 후면 변수
    var faceCamera : Boolean = false

    //카메라 미리보기
    lateinit var texture : SurfaceTexture

    var fileCount : Int = 0
    private val uriList:MutableList<String> = mutableListOf()

    private lateinit var binding : ActivityMainBinding

    lateinit var pageRecyclerAdapter : PageRecyclerAdapter

    companion object{

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.textureView.surfaceTextureListener=textureListener

        binding.textureView.isEnabled=false

/*        binding.textureView.setOnClickListener{
            takePicture()
        }*/

        binding.capture.setOnClickListener {
            takePicture()
        }

        binding.rotate.setOnClickListener{
            when(faceCamera){
                true ->{
                    faceCamera=false
                    cameraDevice?.close()
                    openCamera()
                }
                false ->{
                    faceCamera=true
                    cameraDevice?.close()
                    openCamera()
                }
            }
        }

        pageRecyclerAdapter= PageRecyclerAdapter(this)
        //뷰페이지의 어답터 설정
        binding.viewPager.adapter = pageRecyclerAdapter



    }

    override fun onResume() {
        super.onResume()

        if(binding.textureView.isAvailable){
            try {
                openCamera()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            binding.textureView.surfaceTextureListener = textureListener
        }

    }

    override fun onPause() {
        closeCamera()
        Log.d("onPause","onPause")
        super.onPause()
    }

    private var textureListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            // 지정된 SurfaceTexture 를 파괴하고자 할 때 호출된다
            // true 를 반환하면 메서드를 호출한 후 SurfaceTexture 에서 랜더링이 발생하지 않는다
            // 대부분의 응용프로그램은 true 를 반환한다
            // false 를 반환하면 SurfaceTexture#release() 를 호출해야 한다
            return false
        }

        //textureView 가 사용가능한 상태일 때 호출
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {

            openCamera()
            //카메라 미리보기 회전 시키기
            transformImage(width,height)
        }

    }

    // openCamera() 메서드는 TextureListener 에서 SurfaceTexture 가 사용 가능하다고 판단했을 시 실행된다
    private fun openCamera() {
        Log.e(TAG, "openCamera() : openCamera()메서드가 호출되었음")

        // 카메라의 정보를 가져와서 cameraId 와 imageDimension 에 값을 할당하고, 카메라를 열어야 하기 때문에
        // CameraManager 객체를 가져온다
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            // CameraManager 에서 cameraIdList 의 값을 가져온다
            // FaceCamera 값이 true 이면 전면, 아니면 후면 카메라
            cameraId = if (faceCamera) {
                manager.cameraIdList[1]
            }else {
                manager.cameraIdList[0]
            }

            Log.e(TAG, "CameraID = $cameraId")
            // 선택한 카메라의 특징을 알 수 있다
            val characteristics = manager.getCameraCharacteristics(cameraId!!)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            // SurfaceTexture 에 사용할 Size 값을 map 에서 가져와 imageDimension 에 할당해준다
            imageDimension = map!!.getOutputSizes<SurfaceTexture>(SurfaceTexture::class.java)[0]

            Log.e(TAG, "preview Size width:" + imageDimension.getWidth() + ", height" + imageDimension.height)

            // 카메라를 열기전에 카메라 권한, 쓰기 권한이 있는지 확인한다
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED // 카메라 권한없음
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // 쓰기권한 없음
                // 카메라 권한이 없는 경우 권한을 요청한다
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
                return
            }

            // CameraManager.openCamera() 메서드를 이용해 인자로 넘겨준 cameraId 의 카메라를 실행한다
            // 이때, stateCallback 은 카메라를 실행할때 호출되는 콜백메서드이며, cameraDevice 에 값을 할달해주고, 카메라 미리보기를 생성한다
            manager.openCamera(cameraId!!, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    // openCamera() 메서드에서 CameraManager.openCamera() 를 실행할때 인자로 넘겨주어야하는 콜백메서드
    // 카메라가 제대로 열렸으면, cameraDevice 에 값을 할당해주고, 카메라 미리보기를 생성한다
    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "stateCallback : onOpened")

            // MainActivity 의 cameraDevice 에 값을 할당해주고, 카메라 미리보기를 시작한다
            // 나중에 cameraDevice 리소스를 해지할때 해당 cameraDevice 객체의 참조가 필요하므로,
            // 인자로 들어온 camera 값을 전역변수 cameraDevice 에 넣어 준다
            cameraDevice = camera

            // createCameraPreview() 메서드로 카메라 미리보기를 생성해준다
            createCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "stateCallback : onDisconnected")

            // 연결이 해제되면 cameraDevice 를 닫아준다
            cameraDevice!!.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.d(TAG, "stateCallback : onError")

            closeCamera()

        }

    }

    // openCamera() 에 넘겨주는 stateCallback 에서 카메라가 제대로 연결되었으면
    // createCameraPreviewSession() 메서드를 호출해서 카메라 미리보기를 만들어준다
    private fun createCameraPreviewSession() {
        try {

            // 캡쳐세션을 만들기 전에 프리뷰를 위한 Surface 를 준비한다
            // 레이아웃에 선언된 textureView 로부터 surfaceTexture 를 얻을 수 있다
            texture = binding.textureView.surfaceTexture!!

            // 미리보기를 위한 Surface 기본 버퍼의 크기는 카메라 미리보기크기로 구성
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)


            // 미리보기를 시작하기 위해 필요한 출력표면인 surface
            val surface : Surface = Surface(texture)


            // 미리보기 화면을 요청하는 RequestBuilder 를 만들어준다.
            // 이 요청은 위에서 만든 surface 를 타겟으로 한다
            captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(surface)

            // 위에서 만든 surface 에 미리보기를 보여주기 위해 createCaptureSession() 메서드를 시작한다
            // createCaptureSession 의 콜백메서드를 통해 onConfigured 상태가 확인되면
            // CameraCaptureSession 을 통해 미리보기를 보여주기 시작한다
            cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Log.d(TAG, "Configuration change")
                }

                override fun onConfigured(session: CameraCaptureSession) {
                    if(cameraDevice == null) {
                        // 카메라가 이미 닫혀있는경우, 열려있지 않은 경우
                        return
                    }
                    // session 이 준비가 완료되면, 미리보기를 화면에 뿌려주기 시작한다
                    cameraCaptureSessions = session

                    captureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

                    try {
                        cameraCaptureSessions!!.setRepeatingRequest(captureRequestBuilder!!.build(), null, null)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.capture.isEnabled=true
                        }
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

            }, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun transformImage(width: Int, height: Int) {
        if (binding.textureView == null) {
            return
        } else try {
            run {
                val matrix = Matrix()
                val rotation = windowManager.defaultDisplay.rotation
                val textureRectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
                val previewRectF = RectF(0F, 0F, textureView.height.toFloat(), textureView.width.toFloat())
                val centerX = textureRectF.centerX()
                val centerY = textureRectF.centerY()
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                    previewRectF.offset(centerX - previewRectF.centerX(), centerY - previewRectF.centerY())
                    matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.FILL)
                    val scale = Math.max(width.toFloat() / width, height.toFloat() / width)
                    matrix.postScale(scale, scale, centerX, centerY)
                    matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
                }
                binding.textureView.setTransform(matrix)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 사진찍을 때 호출하는 메서드
    private fun takePicture() {
        try {
            binding.capture.isEnabled=false
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
            var jpegSizes: Array<Size>? = null
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.getOutputSizes(ImageFormat.JPEG)

            var width = jpegSizes[0].width
            var height = jpegSizes[0].height

            Log.d(TAG,"width:$width height:$height")

            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)

            val outputSurface = ArrayList<Surface>(2)
            outputSurface.add(imageReader!!.surface)
            outputSurface.add(Surface(textureView!!.surfaceTexture))

            // ImageCapture를 위한 CaputureRequest.Builder 객체
            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(imageReader!!.surface)

            // 이미지를 캡처하는 순간에 제대로 사진 이미지가 나타나도록 3A를 자동으로 설정
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            // 사진의 rotation 을 설정해준다
            val rotation = windowManager.defaultDisplay.rotation
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation))
//            var file = File(Environment.getExternalStorageDirectory().toString() + "/pic${fileCount}.jpg")

            // 이미지를 캡처할 때 자동으로 호출된다.
            val readerListener = object : ImageReader.OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader?) {

                    var image : Image? = null

                        image = imageReader!!.acquireLatestImage()

                        val buffer : ByteBuffer = image!!.planes[0].buffer

                        val bytes = ByteArray(buffer.capacity())
                        buffer.get(bytes)
                        var bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)

                        val file :File = createImageFile(bitmap)

                        Log.d(TAG, "파일 경로 확인: ${file.absoluteFile}")

/*                        var output: OutputStream? = null
                        try {
                            output = FileOutputStream(file)
                            output.write(bytes)
                        } finally {
                            output?.close()

                            var uri = Uri.fromFile(file)
                            Log.d(TAG, "uri 제대로 잘 바뀌었는지 확인 ${uri}")*/

                            // 프리뷰 이미지에 set 해줄 비트맵을 만들어준다
/*                            var bitmap1: Bitmap = BitmapFactory.decodeFile(file.path)
                            Log.d(TAG, "uri 제대로 잘 바뀌었는지 확인 ${bitmap1}")*/


                            var rotateMatrix = Matrix()

                            // landscape 일때 설정
                            if(faceCamera){
                                rotateMatrix.postRotate(360F)
                                rotateMatrix.setScale(-1f,1f)
                            }else{
                                rotateMatrix.postRotate(0F)
                                rotateMatrix.setScale(1f,1f)
                            }
                            var rotatedBitmap: Bitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.width, bitmap.height, rotateMatrix, false)


                            pageRecyclerAdapter.addItem(FileVo(rotatedBitmap,file.absolutePath,""))
                            CoroutineScope(Dispatchers.Main).launch {
                                pageRecyclerAdapter.notifyDataSetChanged()
                            }

                            Log.d("page 리사이클러뷰 확인","${pageRecyclerAdapter.getItemList()}")
                    
/*                            CoroutineScope(Dispatchers.Main).launch {
                                binding.imgPreviewImage.setImageBitmap(bitmap)
                            }*/

                            // 리사이클러뷰 갤러리로 보내줄 uriList 에 찍은 사진의 uri 를 넣어준다
//                            uriList.add(0, uri.toString())

//                            fileCount++

                        }


            }


            // 이미지를 캡처하는 작업은 메인 스레드가 아닌 스레드 핸들러로 수행한다.
            val thread = HandlerThread("CameraPicture")
            thread.start()
            val backgroundHandler = Handler(thread.looper)

            // imageReader 객체에 위에서 만든 readerListener 를 달아서, 이미지가 사용가능하면 사진을 저장한다
            imageReader!!.setOnImageAvailableListener(readerListener, backgroundHandler)

            // 사진 이미지를 캡처한 이후 호출되는 메소드
            val captureListener = object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    super.onCaptureCompleted(session, request, result)
                    Toast.makeText(this@MainActivity, "사진이 촬영되었습니다", Toast.LENGTH_SHORT).show()
                    createCameraPreviewSession()
                }
            }

            // outputSurface 에 위에서 만든 captureListener 를 달아, 캡쳐(사진 찍기) 해주고 나서 카메라 미리보기 세션을 재시작한다
            cameraDevice!!.createCaptureSession(outputSurface, object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {}

                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, backgroundHandler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

            }, null)


        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun createImageFile(bitmap: Bitmap): File {
        val timeStamp = SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(Date())
        val fileStamp1 : String = SimpleDateFormat("yyyy_MM_dd").format(Date())
        val imageFileName = "image$timeStamp"
        val storageDir: File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES+File.separator+fileStamp1)
        if (!storageDir!!.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }

/*        val image = File.createTempFile(
            imageFileName,  //prefix
            ".jpg",  //suffix
            storageDir //directory
        )*/

        val image1= storageDir.absolutePath+File.separator+imageFileName+".jpeg"
        val image : File = File(image1)

        /**
         * https://arabiannight.tistory.com/entry/257 참조
         * Bitmap을 파일화 시킴
         */
//        val fileCacheItem = File(image.absolutePath)
        var outputStream: OutputStream? = null
        try {
//            fileCacheItem.createNewFile()
            outputStream = FileOutputStream(image1)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return image
    }

    // 카메라 객체를 시스템에 반환하는 메서드
    // 카메라는 싱글톤 객체이므로 사용이 끝나면 무조건 시스템에 반환해줘야한다
    // 그래야 다른 앱이 카메라를 사용할 수 있다
    private fun closeCamera() {
        if (null != cameraDevice) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }


    //picture 삭제
    override fun onRemoveClick(position: Int) {
        Log.d("삭제","삭제번호:$position")

        val fileVos : MutableList<FileVo> = pageRecyclerAdapter.getItemList()
        //파일 삭제 하기
        removeDir(fileVos[position].filePath)
        
        fileVos.removeAt(position)



        for (index in fileVos.indices){
            fileVos[index] = FileVo(fileVos[index].bitmap!!,fileVos[index].filePath,fileVos[index].filename,fileVos[index].start,"${fileVos.size}")
            pageRecyclerAdapter.notifyItemChanged(index)
        }


        Log.d("asdsad","사이즈 확인:${fileVos.size}")

        // 7. 어댑터에서 RecyclerView에 반영하도록 합니다.
        pageRecyclerAdapter.notifyItemRemoved(position)
        pageRecyclerAdapter.notifyItemRangeChanged(position, fileVos.size)
//        pageRecyclerAdapter.notifyDataSetChanged()

    }

    private fun removeDir( dirName: String) : Unit{

        Log.d("dirName", dirName)

        val mRootPath : String = dirName
        val file : File = File(mRootPath)

        file.delete()

    }


    //picture 수정
    override fun onModifyClick(position: Int) {
        Log.d("수정","수정번호:$position")
        val fileVos : MutableList<FileVo> = pageRecyclerAdapter.getItemList()

        val builder = AlertDialog.Builder(this)

        val view: View = LayoutInflater.from(this).inflate(R.layout.picture_edit, null, false)
        builder.setView(view)
        val Picture_Name : EditText = view.findViewById(R.id.picture_name) as EditText
        val Button_Submit : Button = view.findViewById(R.id.dialog_submit) as Button

        Picture_Name.setText(fileVos[position].filename)

/*        Picture_Name.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })*/

        val dialog = builder.create()

        Button_Submit.setOnClickListener{

            var filename : String = ""
            filename="${Picture_Name.text}"

            val Item = FileVo(fileVos[position].bitmap!!,fileVos[position].filePath,filename,fileVos[position].start,fileVos[position].end)
            // 8. ListArray에 있는 데이터를 변경하고
            fileVos[position] = Item

            // 9. 어댑터에서 RecyclerView에 반영하도록 합니다.
            pageRecyclerAdapter.notifyItemChanged(position)

            dialog.dismiss()

        }
        dialog.show()


    }


}