package pt.aubay.cv.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;


import org.primefaces.model.StreamedContent;

import org.primefaces.model.UploadedFile;

import org.omnifaces.util.Faces;

import pt.aubay.cv.control.ControllerRequest;
import pt.aubay.cv.models.Request;
import pt.aubay.cv.models.SSLEmail;
import pt.aubay.cv.models.Status;



@Named("ReqBean")
@ViewScoped

public class Requestbean implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Request request = new Request();

	private UploadedFile cvOrig;
	private UploadedFile cvAubay;
	private StreamedContent downloadAubay;

	private List<Request> requestList;
	private List<Request> requestListAll;
	private List<Request> requestListAprovado;
	private List<Request> requestListNotAprovado;



	@Inject
	private ControllerRequest cr;
	
	@Inject
	private AdminEmailBean admEmail;

	public AdminEmailBean getAdmEmail() {
		return admEmail;
	}

	public void setAdmEmail(AdminEmailBean admEmail) {
		this.admEmail = admEmail;
	}

	public UploadedFile getCvAubay() {
		return cvAubay;
	}

	public void setCvAubay(UploadedFile cvAubay) {
		this.cvAubay = cvAubay;
	}

	public List<Request> getRequestList() {
		return requestList;
	}

	public List<Request> getRequestListAll(){
		return requestListAll;
	}

	public List<Request> getRequestListAprovado() {
		return requestListAprovado;
	}

	public void setRequestListAprovado(List<Request> requestListAprovado) {
		this.requestListAprovado = requestListAprovado;
	}
	public List<Request> getRequestListNotAprovado() {
		return requestListNotAprovado;
	}

	public void setRequestListNotAprovado(List<Request> requestListNotAprovado) {
		this.requestListNotAprovado = requestListNotAprovado;
	}
	public StreamedContent getDownloadAubay() {
        return downloadAubay;
    }

    public void setDownloadAubay(StreamedContent downloadAubay) {
        this.downloadAubay = downloadAubay;
    }


	@PostConstruct
	public void loadRequests() {
		requestList = cr.getReq();
		requestListAll = cr.getReqAll();
		requestListAprovado = cr.getReqAllAprovado();
		requestListNotAprovado = cr.getAllNotAprovado();

		// System.out.println(requestList.size());
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public UploadedFile getCvOrig() {
		return cvOrig;
	}

	public void setCvOrig(UploadedFile cvOrig) {
		this.cvOrig = cvOrig;
	}

	public ControllerRequest getCr() {
		return cr;
	}

	public void setCr(ControllerRequest cr) {
		this.cr = cr;
	}

	public void createReq() {
		request.setEstado(Status.INICIADO);
		cr.createRequest(request);

		FacesMessage msg = new FacesMessage("Pedido registrado.");
		FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);
	}


	public void removeReq(Request request) {
		cr.removeRequest(request);
		//requestList = cr.getReq();
		//loadRequests();
		requestListAprovado = cr.getReqAllAprovado();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Pedido removido");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}    

	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Pedido Editado");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		Request request = (Request) event.getObject();
		cr.updateReq(request);
		requestList = cr.getReq();
		requestListNotAprovado = cr.getAllNotAprovado();
		
//		if(request.getDeadline()!=null) {
//			DateFormat date =  DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.HOUR0_FIELD, Locale.);
//			String dateFormat = date.format(request.getDeadline());
//			System.out.println(dateFormat);
//		}
//		

		
		if(request.getRecruiter().getEmail().contains("@")) {
			String bodyMail = "O candidato com o nome de " + request.getCandidateName() + " foi lhe atribuido a si até á Data Limite de " + request.getDeadline();
			
			
			
			this.sendMail(request.getRecruiter().getEmail(), bodyMail);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Notificação", "Email para recruta foi enviado."));
		}
	


	}

	public void onRowEditOngoingList(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Pedido Editado");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		Request request = (Request) event.getObject();
		
		if(request.getEstado()==null) {
			request.setEstado(Status.INICIADO);
		}
		

		if(request.getEstado() == Status.APROVADO) {
			if(request.getManager().getEmail().contains("@")) {
				String bodymail = "O candidato de nome de " + request.getCandidateName() + " foi concluido.";
				this.sendMail(request.getManager().getEmail(), bodymail);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notificação Email", "Email enviado para admistrador"));
			}
		}else if(request.getEstado()== Status.REPROVADO) {
			if(request.getRecruiter().getEmail().contains("@")) {
				String bodymail = "Informamos que o pedido foi reprovado, por favor verificar os comentarios: \n" + request.getComment();
				this.sendMail(request.getRecruiter().getEmail(), bodymail);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notificação Email", "Email enviado para recrutador "+ request.getRecruiter().getEmail()));
				
			}
		}
		cr.updateReq(request);
		
		requestListNotAprovado = cr.getAllNotAprovado();
		requestListAprovado = cr.getReqAllAprovado();
		
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição Cancelada");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void uploadOrig() {
		try { 
			String dir = System.getProperty("jboss.server.base.dir") + "/deployments/uploadedCVs/cvOrig/";
			File folder = new File(dir);
			folder.mkdirs();

			File file = new File(dir, cvOrig.getFileName());

			OutputStream out = new FileOutputStream(file);
			out.write(cvOrig.getContents());
			out.close();

			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage("Upload completo",
							"O arquivo " + cvOrig.getFileName() + " foi salvo em " + file.getAbsolutePath()));
			request.setCvOrigPath(file.getAbsolutePath());

		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", e.getMessage()));
		}
		createReq();
	}


	public void sendMail(String mail, String body) {
		SSLEmail.SSl(mail, body);
	}
	
    

    public void uploadAubay(Request request) {
        try {
            String dir = System.getProperty("jboss.server.base.dir") + "/deployments/uploadedCVs/cvAubay/";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, cvAubay.getFileName());

            OutputStream out = new FileOutputStream(file);
            out.write(cvAubay.getContents());
            out.close();
            
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage("Upload completo",
                            "O arquivo " + cvAubay.getFileName() + " foi salvo em " + file.getAbsolutePath()));
            request.setCvAubayPath(file.getAbsolutePath());

        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", e.getMessage()));
        }
        request.setEstado(Status.PREAPROVADO);
        cr.updateReq(request);
        loadRequests();
        System.out.println(admEmail.getActiveadmEmailListString());
        
        if(admEmail.getActiveadmEmailListString().contains("@")) {
        	this.sendMail(admEmail.getActiveadmEmailListString(), "OLA");
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação", "Email foi enviado"));
        }
        
      
    }
    
    
    public void download(String filePath) throws IOException{
       File file = new File(filePath);
        Faces.sendFile(file, true);
           /* InputStream input = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(
                    filePath);
            return  new DefaultStreamedContent(input, "application/octet-stream", "downloaded.pdf" );*/
    }
   

    
}
