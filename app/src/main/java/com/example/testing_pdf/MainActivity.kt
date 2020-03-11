package com.example.testing_pdf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun createPDF(view: View) {
        createAndSend()
    }

    private fun createAndSend() {
        val newFile = createFile("pdfDocuments","TestingFile.pdf")
        val fileName = newFile.toString()
        val contentUri: Uri = getUriForFile(
            applicationContext,
            "com.example.testing_pdf", newFile
        )

        if (newFile.exists()) {
            newFile.delete()
        }

        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(fileName))
            document.open()
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("JAPM")
            document.addCreator("Jorge Peroza")

            val colorAccent = BaseColor(0, 153, 204, 255)
            val headingFontSize = 20.0f
            val valueFontSize = 26.0f
                val fontName = BaseFont.createFont(
                    "assets/fonts/brandon_medium.otf",
                    "UTF-8",
                    BaseFont.EMBEDDED
                )
            val titleStyle = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle =
                Font(fontName, headingFontSize, Font.NORMAL, colorAccent)
            val valueStyle =
                Font(Font.FontFamily.TIMES_ROMAN, valueFontSize, Font.NORMAL, BaseColor.BLACK)

            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleStyle)
            addNewItem(document, "Order No", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, "123123", Element.ALIGN_LEFT, valueStyle)

            addLineSeparator(document)

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, "11/03/2004", Element.ALIGN_LEFT, valueStyle)

            document.close()

            sendEmail(contentUri)

        } catch (e: Exception) {
        }
    }

    private fun createFile(folderName:String,fileName:String): File {
        val dir = File(
            applicationContext.filesDir.toString()
                    + File.separator + folderName + File.separator
        )
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }

    /*function to send the email of the order*/
    private fun sendEmail(contentUri: Uri) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM,contentUri)
        emailIntent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("cicelcup@hotmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ejemplo")
        intent.putExtra(Intent.EXTRA_TEXT,"Esto es un email de prueba")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.selector = emailIntent
        startActivity(Intent.createChooser(intent,"Prueba"))
    }
}
