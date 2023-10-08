package com.example.documentconvert.Services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import javax.activation.DataSource;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.*;


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
*/		File wordFile = new File("/Users/macos/IdeaProjects/documentConvert/src/main/resources/static/Docx/word.docx");

		return convertWordToPDFToByteArrayResource(wordFile,"/Users/macos/IdeaProjects/documentConvert/src/main/resources/static/Docx");
		/*byte[] pdfByteArray = convertWordToPDFToByte(wordFile);

		*//*MimeBodyPart attachment = new MimeBodyPart(new InternetHeaders(),pdfByteArray);
		attachment.setHeader("Content-Type","application/pdf");
		attachment.setHeader("Content-Disposition","attachment; filename=\"" + MimeUtility.encodeText("docs","UTF-8","B")+ "\"");

		DataSource dataSource = attachment.getDataHandler().getDataSource();*//*

		return new ByteArrayResource(pdfByteArray);*/
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
			File wordFile = new File("/Users/macos/IdeaProjects/documentConvert/src/main/resources/static/Docx/word.docx");
			File pdfFile = new File("/Users/macos/IdeaProjects/documentConvert/src/main/resources/static/Docx/word.pdf");

//			converter.convert(wordFile).as(DocumentType.DOCX).to(pdfFile).as(DocumentType.PDF).execute();

			convertWordToPDF(wordFile,pdfFile);
			System.out.println("Conversion complete!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void convertWordToPDF(File inputWordFile, File outputPdfFile) throws IOException, InterruptedException {
		String command = "/Applications/LibreOffice.app/Contents/MacOS/soffice --headless --convert-to pdf " + inputWordFile.getAbsolutePath() + " --outdir " + outputPdfFile.getParentFile().getAbsolutePath();

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		process.waitFor();

		// Handle the result of the conversion process if needed
	}

	public static ByteArrayResource convertWordToPDFToByteArrayOutputStream(File inputWordFile) throws IOException, InterruptedException {
		String command = "/Applications/LibreOffice.app/Contents/MacOS/soffice --headless --convert-to pdf " + inputWordFile.getAbsolutePath();

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		process.waitFor();

		// Capture the output of the process directly into a ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (InputStream inputStream = process.getInputStream()) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// Handle the exception as needed
			e.printStackTrace();
		} finally {
			process.destroy(); // Ensure the process is properly closed
		}

		// Create ByteArrayResource from the resulting byte array
		byte[] pdfBytes = outputStream.toByteArray();
		return new ByteArrayResource(pdfBytes);
	}

	public static ByteArrayResource convertWordToPDFToByteArrayResource(File inputWordFile, String outputPdfFilePath) throws IOException, InterruptedException {
		String command = "/Applications/LibreOffice.app/Contents/MacOS/soffice --headless --convert-to pdf " + inputWordFile.getAbsolutePath() + " --outdir " + outputPdfFilePath;

		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		process.waitFor();

		// Read the PDF file into a byte array
		File pdfFile = new File(outputPdfFilePath, inputWordFile.getName().replace(".docx", ".pdf"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// Handle the exception as needed
			pdfFile.delete();
			e.printStackTrace();
		}

		// Create ByteArrayResource from the resulting byte array
		byte[] pdfBytes = outputStream.toByteArray();
		pdfFile.delete();
		return new ByteArrayResource(pdfBytes);
	}


}