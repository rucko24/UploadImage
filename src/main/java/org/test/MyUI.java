package org.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;


@Theme("mytheme")
@Widgetset("org.test.MyAppWidgetset")
public class MyUI extends UI {
	
	private ImageUploader receiver = new ImageUploader();
	private Image image = new Image("Upload image");
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	final HorizontalLayout layout = new HorizontalLayout();
    	layout.setMargin(true);
    	layout.setSpacing(true);
   
    	image.setVisible(false);
    	
    	Upload upload = new Upload("Cargar imagen aqui",receiver);
    	upload.setButtonCaption("empezar");
    	upload.addSucceededListener(receiver);
    	
    	Panel panel2 = new Panel("Cool almacen de imagenes");
    	VerticalLayout verticalLayout = new VerticalLayout();
    	verticalLayout.addComponents(upload,image);
    	panel2.setContent(verticalLayout);
    	long s = System.nanoTime();
    	layout.addComponents(panel2);
    	setContent(layout);
    }

    public class ImageUploader implements Receiver, SucceededListener {
    	
    	public File file;
		@Override
		public void uploadSucceeded(SucceededEvent event) {
			image.setVisible(true);
			image.setSource(new FileResource(file));
			
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null;
			try {
				
				file = new File("/home/rubn/Imágenes/"+filename);
				fos = new FileOutputStream(file);
				
			}catch(final java.io.FileNotFoundException e) {
				
				//new Notification("No se puede abrir el archivo",e.getMessage(),Type.TRAY_NOTIFICATION.ERROR_MESSAGE).show(Page.getCurrent());
				notification(e.getMessage());
			
				return null;
			}
			return fos;
		}
		
		public Notification notification(String descripcion) {
			
			Notification notification = new Notification("Error no se pudo abrir",Type.ERROR_MESSAGE);
			notification.setDescription(descripcion);
			notification.setIcon(new ThemeResource("../runo/icons/16/error.png"));
			notification.setPosition(Position.BOTTOM_RIGHT);
			notification.show(UI.getCurrent().getPage());
			return notification;
		}
    	
    };
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
