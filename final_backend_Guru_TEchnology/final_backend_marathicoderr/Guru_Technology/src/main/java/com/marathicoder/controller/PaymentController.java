 
 
 
package com.marathicoder.controller;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
import javax.mail.internet.MimeMessage;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.service.NotificationService;
 
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/payments")
public class PaymentController {
 
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private NotificationService notificationService; 
  
 
//    @PostMapping("/pay/{studentId}")
//    public ResponseEntity<Map<String, Object>> makePayment(
//            @PathVariable String studentId,
//            @RequestParam Double amount) {
// 
//        Student student = studentRepository.findByStudentId(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
// 
//        // Amount ko set karo (frontend se aaya hua)
//        student.setAmount(amount);
// 
//        // Status update karo
//        student.setPaymentStatus("PAID");
// 
//        studentRepository.save(student);
// 
//        // ✅ JSON response me sirf studentId bhejna
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("studentId", student.getStudentId());
// 
//        return ResponseEntity.ok(response);
//    }
// 
    
    
    @PostMapping("/pay/{studentId}")
    public ResponseEntity<Map<String, Object>> makePayment(
            @PathVariable String studentId,
            @RequestParam Double amount) {

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update payment
        student.setAmount(amount);
        student.setPaymentStatus("PAID");
        studentRepository.save(student);

        // ✅ Send notification to student
        NotificationEntity notification = new NotificationEntity();
        notification.setMessage("💰 Payment of ₹" + amount + " received successfully!");
        notification.setSenderRole("ADMIN");
        notification.setReceiverRole("STUDENT");
        notification.setReceiverEmail(student.getEmail());
        notification.setReadStatus(false);
        notificationService.send(notification);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("studentId", student.getStudentId());

        return ResponseEntity.ok(response);
    }
    
    
    // ✅ Fetch summary (id, name, email, course, payment)
    @GetMapping("/payment")
    public List<Map<String, Object>> getAllPaymentSummary() {
        List<Student> students = studentRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
 
        for (Student s : students) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("studentId", s.getStudentId());
            data.put("name", s.getName());
            data.put("email", s.getEmail());
            data.put("course", s.getCourse());
 
            // ✅ payment info
            data.put("paymentStatus", s.getPaymentStatus());
            data.put("amount", s.getAmount());
 
            result.add(data);
        }
        return result;
    }
 
    // ✅ NEW: Admin ke liye total payment details
    @GetMapping("/admin/payments")
    public ResponseEntity<List<Map<String, Object>>> getAllPaymentsForAdmin() {
        List<Student> students = studentRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
 
        for (Student s : students) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("studentId", s.getStudentId());
            data.put("name", s.getName());
            data.put("email", s.getEmail());
            data.put("course", s.getCourse());// ✅ Email add kiya
            data.put("amountPaid", s.getAmount() != null ? s.getAmount() : 0.0);
            data.put("paymentStatus", s.getPaymentStatus() != null ? s.getPaymentStatus() : "PENDING");
            response.add(data);
        }
 
        return ResponseEntity.ok(response);
    }
    
    
 
 
 
    
//    @GetMapping("/generate/{email}")
//    public ResponseEntity<byte[]> generatePayslip(@PathVariable String email)
//    {
//    	try {
// 
//    		
//    		Student student = studentRepository.findByEmailIgnoreCase(email.trim())
//      	        .orElseThrow(() -> new RuntimeException("Student not found"));
//    		
//    		
//    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    		PDDocument document = new PDDocument(); PDPage page = new PDPage();
//    		document.addPage(page); PDPageContentStream contentStream = new PDPageContentStream(document, page); contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14); contentStream.beginText();
//    		contentStream.newLineAtOffset(100, 700);
//    		contentStream.showText("Payslip for: " + email);
//    		contentStream.newLineAtOffset(0, -20);
//    		 contentStream.showText("Name: " + student.getName());
//           contentStream.newLineAtOffset(0, -20);
//           contentStream.showText("StudentID: " + student.getStudentId());
//           contentStream.newLineAtOffset(0, -20);
//           contentStream.showText("Course: " + student.getCourse()); // ✅ No error now
//         contentStream.newLineAtOffset(0, -20);
//    		contentStream.showText("Course Fee:"+student.getAmount());
//    		contentStream.newLineAtOffset(0, -20);
//    		contentStream.showText("Payment:"+student.getPaymentStatus());
//    		contentStream.newLineAtOffset(0, -20);
////    		contentStream.showText("Deductions: 5,000");
////    		contentStream.newLineAtOffset(0, -20);
////    		contentStream.showText("Net Salary: 45,000");
//    		contentStream.endText();
//    		contentStream.close();
//    		document.save(baos);
//    		document.close();
//    		byte[] pdfBytes = baos.toByteArray();
//    		HttpHeaders headers = new HttpHeaders();
//    		headers.setContentType(MediaType.APPLICATION_PDF);
//    		headers.setContentDispositionFormData("attachment", "payslip.pdf");
//    		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    		} catch (Exception e)
//    	{
//    			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    			}
//    	}
// 
 
    
 // ✅ Generate Payslip
    @GetMapping("/generate/{email}")
    public ResponseEntity<byte[]> generatePayslip(@PathVariable String email) {
        try {
            Student student = studentRepository.findByEmailIgnoreCase(email.trim())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Payslip for: " + email);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Name: " + student.getName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("StudentID: " + student.getStudentId());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Course: " + student.getCourse());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Course Fee: " + student.getAmount());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Payment: " + student.getPaymentStatus());
            contentStream.endText();
            contentStream.close();

            document.save(baos);
            document.close();

            byte[] pdfBytes = baos.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payslip.pdf");

            // ✅ Send notification to student
            NotificationEntity notification = new NotificationEntity();
            notification.setMessage("📄 Payslip generated for your recent payment.");
            notification.setSenderRole("ADMIN");
            notification.setReceiverRole("STUDENT");
            notification.setReceiverEmail(student.getEmail());
            notification.setReadStatus(false);
            notificationService.send(notification);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
 
 
}
 