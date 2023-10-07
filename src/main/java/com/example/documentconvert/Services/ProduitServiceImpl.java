package com.example.documentconvert.Services;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import javax.activation.DataSource;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
@Slf4j
public class ProduitServiceImpl implements IProduitService {




	@Override
	public ByteArrayResource convertDocxToPDF() {
	try {
		XWPFDocument document = new XWPFDocument(new FileInputStream("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.docx"));
		ByteArrayOutputStream pdfOutputStrem = new ByteArrayOutputStream();

		document.write(pdfOutputStrem);

		IConverter converter = LocalConverter.builder().build();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStrem.toByteArray());
		ByteArrayOutputStream pdfByteArrayOutputStream = new ByteArrayOutputStream();
		converter.convert(inputStream).as(DocumentType.DOCX).to(pdfByteArrayOutputStream).as(DocumentType.PDF).execute();

		byte[] pdfByteArray = pdfByteArrayOutputStream.toByteArray();

		MimeBodyPart attachment = new MimeBodyPart(new InternetHeaders(),pdfByteArray);
		attachment.setHeader("Content-Type","application/pdf");
		attachment.setHeader("Content-Disposition","attachment; filename=\"" + MimeUtility.encodeText("docs","UTF-8","B")+ "\"");

		DataSource dataSource = attachment.getDataHandler().getDataSource();
		converter.kill();
		document.close();
		return new ByteArrayResource((dataSource).getInputStream().readAllBytes());
	}catch (Exception e)
	{
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public void convertDocx() {
	try {
		XWPFDocument document = new XWPFDocument(new FileInputStream("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.docx"));
		ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

		document.write(pdfOutputStream);

		IConverter converter = LocalConverter.builder().build();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
		//converter.convert(inputStream).as(DocumentType.DOCX).to(pdfByteArrayOutputStream).as(DocumentType.PDF).execute();
		converter.convert(inputStream).as(DocumentType.DOCX).to(new FileOutputStream("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/doc.pdf")).as(DocumentType.PDF).execute();
		converter.kill();
		document.close();
		pdfOutputStream.close();
		inputStream.close();

	}catch (Exception e)
	{
		e.printStackTrace();
	}
	}




}