package com.rebliss.view.activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.rebliss.R
import kotlinx.android.synthetic.main.activity_view_document.*
import java.io.*
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.util.concurrent.Executors


class ViewDocumentActivity : BaseActivity() {
    var document_url: String = ""
    var file_type: String = ""
    var uri: URI? = null
    var url: URL? = null
    var mImage: Bitmap? = null
    var imageUri: Uri? = null
    var queId: Long? = null
    lateinit var receiver: BroadcastReceiver
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_document)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val bundle = intent.extras
        document_url = bundle?.getString("doc_path")!!
        file_type = bundle.getString("file_type")!!

        // Declaring and initializing an Executor and a Handler
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

        if (file_type.equals("1"))
        {
            imgDocument.visibility = View.VISIBLE
            Glide.with(this).load(document_url).into(imgDocument)
        }
        else
        {
            imgDocument.visibility = View.GONE
            idPDFView.visibility = View.VISIBLE
            downloadPdfFromInternet(document_url, getRootDirPath(this),"myFile.pdf")
        }


        btnShare.setOnClickListener {
            if (file_type.equals("1")) {
                myExecutor.execute {
                    mImage = mLoad(document_url)
                      sharePalette(mImage!!)
                }
            }
            else
            {
                Toast.makeText(this@ViewDocumentActivity, "Download.....", Toast.LENGTH_LONG).show()
                downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse(document_url)
                val request: DownloadManager.Request = DownloadManager.Request(uri)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                queId = downloadManager.enqueue(request)
            }
        }
        initBroadcastReceiver()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        btnDownloadDoc.setOnClickListener {
            if (file_type.equals("1")) {
                myExecutor.execute {
                    mImage = mLoad(document_url)
                    myHandler.post {
                        imgDocument.setImageBitmap(mImage)
                        if (mImage != null) {
                                mSaveMediaToStorage(mImage)
                        }
                    }
                }
            }
            else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(document_url))
                startActivity(browserIntent)
            }
        }
    }

    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            val builder = AlertDialog.Builder(this@ViewDocumentActivity,
                R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.customview_layout_documentpath, null)
            val button = view.findViewById<Button>(R.id.dialogDismiss_button)
            val description = view.findViewById<TextView>(R.id.txtDesc)
            description.text =
                "Image has been saved to "+imageUri
            builder.setView(view)
            builder.setCanceledOnTouchOutside(false)
            builder.show()
            button.setOnClickListener { builder.dismiss() }
        }
    }
    // Function to establish connection and load image
        private fun mLoad(string: String): Bitmap? {
            val url: URL = mStringToURL(string)!!
            Log.e("TAG", "mLoad: "+url )
            val connection: HttpURLConnection?
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                return BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
            return null
        }
    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {

            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        Toast.makeText(this@ViewDocumentActivity, "Please wait....", Toast.LENGTH_LONG).show()
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)

                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        this@ViewDocumentActivity,
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    private fun showPdfFromFile(file: File) {
        idPDFView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@ViewDocumentActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }


    private fun sharePalette(bitmap: Bitmap) {
        val bitmapPath = MediaStore.Images.Media.insertImage(
            contentResolver, bitmap, "Rebliss", "sareImage")
        if(bitmapPath!=null) {
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*|application/pdf|audio/*"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    private fun initBroadcastReceiver() {
        receiver = object : BroadcastReceiver() {
            @SuppressLint("Range")
            override fun onReceive(p0: Context?, p1: Intent?) {
                Log.e("TAG", "onReceive: >>>>>>>>>" )
                val action = p1!!.action
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                    try {


                        var requestQuery = DownloadManager.Query()
                        requestQuery.setFilterById(queId!!)
                        val cursor = downloadManager.query(requestQuery)
                        if (cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                                Toast.makeText(this@ViewDocumentActivity,
                                    "Download Completed",
                                    Toast.LENGTH_SHORT).show()

                                Handler(Looper.getMainLooper()).postDelayed({
                                    val uriString =
                                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                                    val uri = Uri.parse(uriString)
                                    val share = Intent()
                                    share.action = Intent.ACTION_SEND
                                    share.type = "application/pdf"
                                    share.putExtra(Intent.EXTRA_STREAM, uri)
                                    startActivity(share)
                                }, 2000)

                            } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                                Toast.makeText(this@ViewDocumentActivity,
                                    "Download Failed",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    catch (e:NullPointerException)
                    {

                        Log.e("TAG", "onReceive: null pointer " )                    }
                }
            }
        }
    }
}