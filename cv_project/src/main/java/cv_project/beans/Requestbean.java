package cv_project.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cv_project.control.ControllerRequest;

import cv_project.models.Request;


@Named("ReqBean")
@RequestScoped
public class Requestbean {  
<<<<<<< HEAD

   
    
	
private Request request = new Request();
=======
	
>>>>>>> master

    @Inject
    private ControllerRequest cr;

<<<<<<< HEAD
=======
>>>>>>> master

    public Request getRequest() {
            return request;
    }

    public void setRequest(Request request) {
            this.request = request;
    }

    public ControllerRequest getCr() {
            return cr;
    }

    public void setCr(ControllerRequest cr) {
            this.cr = cr;
    }

    public List<Request> getReq(){
            return cr.getReq();
    }

    public void createReq() {
            cr.createRequest(request);
    }

    public void removeReq() {
            cr.removeRequest(request);
    }

    public void updateReq() {
            cr.updateReq();
    }
    
}
