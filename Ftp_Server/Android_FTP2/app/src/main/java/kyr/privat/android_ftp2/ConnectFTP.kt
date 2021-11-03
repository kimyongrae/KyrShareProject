package kyr.privat.android_ftp2

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable
import java.net.URLEncoder

class ConnectFTP constructor() :Serializable {

    companion object{
        val TAG:String = "Connect TFP"
        val serialVersionUID = 1L
    }

    private lateinit var mFTPClient : FTPClient

    //ftp 생성
    constructor(mFTPClient : FTPClient):this(){
        this.mFTPClient = mFTPClient
        this.mFTPClient.controlEncoding = "UTF-8";
    }

    //ftp 서버와 연결
    fun ftpConnect(host: String?, username: String?, password: String?, port: Int): Boolean {
        var result = false
        try {
            mFTPClient.connect(host, port)
            if (FTPReply.isPositiveCompletion(mFTPClient.replyCode)) {
                result = mFTPClient.login(username, password)
                mFTPClient.enterLocalPassiveMode()
            }
        } catch (e: Exception) {
            AppContants.println("Couldn't connect to host")
        }
        return result
    }

    //FTP 서버 연결 끊기
    fun ftpDisconnect(): Boolean {
        var result = false
        try {
            mFTPClient.logout()
            mFTPClient.disconnect()
            result = true
        } catch (e: java.lang.Exception) {
            AppContants.println("Failed to disconnect with server")
        }
        return result
    }

    //현재 작업 경로 가져오기
    fun ftpGetDirectory(): String? {
        var directory: String? = null
        try {
            directory = mFTPClient.printWorkingDirectory()
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't get current directory")
        }
        return directory
    }


    //작업 경로 수정
    fun ftpChangeDirctory(directory: String?): Boolean {
        try {
            mFTPClient.changeWorkingDirectory(directory)
            return true
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't change the directory")
        }
        return false
    }

/*    //파일 리스트 가져오기
    fun ftpGetFileList(directory: String?): Array<String?>? {
        var fileList: Array<String?>? = null
        var i = 0
        try {
            val ftpFiles = mFTPClient.listFiles(directory)
            fileList = arrayOfNulls(ftpFiles.size)
            for (file in ftpFiles) {
                val fileName = file.name
                if (file.isFile) {
                    fileList[i] = "(File) $fileName"
                } else {
                    fileList[i] = "(Directory) $fileName"
                }
                i++
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileList
    }*/


        //파일 리스트 가져오기
    fun ftpGetFileList(directory: String): MutableList<String> {
        var fileList: MutableList<String> = mutableListOf()
        var i = 0
        try {
            val ftpFiles = mFTPClient.listFiles(directory)

            for (file in ftpFiles) {
                val fileName = file.name
                if (file.isFile) {
//                    fileList[i] = "(File) $fileName"
                    fileList.add("(File)$fileName")
                } else {
//                    fileList[i] = "(Directory) $fileName"
                    fileList.add("(Directory)$fileName")
                }
                i++
            }


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileList
    }


    //새로운 directory 생성 및 삭제
    fun ftpCreateDirectory(directory: String?): Boolean {
        var result = false
        try {
            result = mFTPClient.makeDirectory(directory)
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't make the directory")
        }
        return result
    }


    fun ftpDeleteDirectory(directory: String?): Boolean {
        var result = false
        try {
            result = mFTPClient.removeDirectory(directory)
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't remove directory")
        }
        return result
    }


    //파일 삭제
    fun ftpDeleteFile(file: String?): Boolean {
        var result = false
        try {
            result = mFTPClient.deleteFile(file)
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't remove the file")
        }
        return result
    }

    //파일 이름 변경
    fun ftpRenameFile(from: String?, to: String?): Boolean {
        var result = false
        try {
            result = mFTPClient.rename(from, to)
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't rename file")
        }
        return result
    }

    //파일 다운로드
    fun ftpDownloadFile(srcFilePath: String?, desFilePath: String?): Boolean {
        var result = false
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE)
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE)
            val fos = FileOutputStream(desFilePath)
            result = mFTPClient.retrieveFile(srcFilePath, fos)
            fos.close()
        } catch (e: java.lang.Exception) {
            AppContants.println("Download failed")
        }
        return result
    }

    //FTP 서버에 파일 업로드
    fun ftpUploadFile(srcFilePath: String?, desFileName: String?, desDirectory: String?): Boolean {
        var result = false
        try {
            val fis = FileInputStream(srcFilePath)
            if (ftpChangeDirctory(desDirectory)) {
                result = mFTPClient.storeFile(desFileName, fis)
            }
            fis.close()
        } catch (e: java.lang.Exception) {
            AppContants.println("Couldn't upload the file")
        }
        return result
    }




}