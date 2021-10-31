Custom Dialog
버튼을 클릭할 경우 3초동안 커스텀 프로그래스바를 보여주고 종료시킴

1.커스텀 다이얼로그 xml 만들기

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="10dp"
    android:background="@android:color/transparent"
    >

    <ProgressBar
        android:id="@+id/spin_kit"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Please wait..."
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/spin_kit"></TextView>

</androidx.constraintlayout.widget.ConstraintLayout>

<img width="300" alt="커스텀 다이얼로그 xml" src="https://user-images.githubusercontent.com/28819051/139573423-f236103b-e00f-4797-9662-afc95c89f2f8.PNG">

2.커스텀 다이얼로그에 연결

<img width="500" alt="커스텀 다이얼로그 연결" src="https://user-images.githubusercontent.com/28819051/139573441-0fe0c0ef-ed1e-4cfc-8a19-5bd3934646ad.PNG">

3.커스텀 다이얼로그 이벤트 발생시 실행 시키기
<img width="500" alt="커스텀 다이얼로그 실행" src="https://user-images.githubusercontent.com/28819051/139573481-d7ac9336-8d5b-4fe0-8891-1623f5d76ec0.PNG">


