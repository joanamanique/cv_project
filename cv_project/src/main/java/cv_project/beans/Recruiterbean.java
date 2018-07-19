package cv_project.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import cv_project.control.ControllerRecruiter;
import cv_project.models.Request;
import cv_project.models.Manager;
import cv_project.models.Recruiter;

@Named("recruiterbean")
@RequestScoped
public class Recruiterbean {

	////////////////////////////// ATRIBUTS
	 private Recruiter recruiter = new Recruiter();
	 
	 @Inject
	 private ControllerRecruiter cr;
	 
	 
	 ////////////////////////////// GETTER & SETTER
	public Recruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}

	public ControllerRecruiter getCr() {
		return cr;
	}

	public void setCr(ControllerRecruiter cr) {
		this.cr = cr;
	}
	
	/*public void getListO(){
		recruiter.getListO();
	}
	
	public void setList(List<Request> listO){
		recruiter.setListO(listO);
	}*/
	public List<Recruiter> getRec(){
		return cr.getRec();
	}
	
	public void createRec() {
		cr.createRecruiter(recruiter);
		//return "index";
	}
	
	public void removeRec() {
		cr.removeRecruter(recruiter);
	}
	public void updateRec() {
		cr.updateList();
	}
	
	public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Recrutador Editado", ((Recruiter) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edição Cancelada", ((Recruiter) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	

}
