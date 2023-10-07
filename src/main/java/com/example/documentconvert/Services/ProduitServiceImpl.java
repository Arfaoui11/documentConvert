package com.example.documentconvert.Services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import javax.activation.DataSource;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


@Service
@Slf4j
public class ProduitServiceImpl implements IProduitService {




	@Override
	public ByteArrayResource convertDocxToPDF() {
	try {
/*
		XWPFDocument document = new XWPFDocument(new FileInputStream("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.docx"));
		ByteArrayOutputStream pdfOutputStrem = new ByteArrayOutputStream();

		document.write(pdfOutputStrem);

		IConverter converter = LocalConverter.builder().build();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStrem.toByteArray());
		ByteArrayOutputStream pdfByteArrayOutputStream = new ByteArrayOutputStream();
		converter.convert(inputStream).as(DocumentType.DOCX).to(pdfByteArrayOutputStream).as(DocumentType.PDF).execute();
*/		File wordFile = new File("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.docx");


		byte[] pdfByteArray = convertWordToPDFToByte(wordFile);

		MimeBodyPart attachment = new MimeBodyPart(new InternetHeaders(),pdfByteArray);
		attachment.setHeader("Content-Type","application/octet-stream");
		attachment.setHeader("Content-Disposition","attachment; filename=\"" + MimeUtility.encodeText("docs","UTF-8","B")+ "\"");

		DataSource dataSource = attachment.getDataHandler().getDataSource();

		return new ByteArrayResource((dataSource).getInputStream().readAllBytes());
	}catch (Exception e)
	{
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public void convertDocx() {
		try  {
//			IConverter converter = LocalConverter.builder().build();
			File wordFile = new File("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.docx");
			File pdfFile = new File("/var/lib/jenkins/workspace/DevOps-CICD/src/main/resources/static/Docx/word.pdf");

//			converter.convert(wordFile).as(DocumentType.DOCX).to(pdfFile).as(DocumentType.PDF).execute();

			convertWordToPDF(wordFile,pdfFile);
			System.out.println("Conversion complete!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void convertWordToPDF(File inputWordFile, File outputPdfFile) throws IOException, InterruptedException {
		String command = "libreoffice --headless --convert-to pdf " + inputWordFile.getAbsolutePath() + " --outdir " + outputPdfFile.getParentFile().getAbsolutePath();

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		process.waitFor();

		// Handle the result of the conversion process if needed
	}

	public static byte[] convertWordToPDFToByte(File inputWordFile) throws IOException, InterruptedException {
		String command = "libreoffice --headless --convert-to pdf " + inputWordFile.getAbsolutePath() + " --outdir /tmp";

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		process.waitFor();

		// Capture the output of the process
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = process.getInputStream().read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		// Close the output stream
		outputStream.close();

		// Get the resulting byte array
		return outputStream.toByteArray();
	}


}