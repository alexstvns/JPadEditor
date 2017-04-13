/*
Alexander Stevens
4/10/2017
Jpad - Assignment 11
Csis 123B-3183
0495503
 */
package jpad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 *
 * @author Alex
 */
public class Jpad extends Application
{
    
    private Stage stage;
    private TextArea ta = new TextArea();
    private String fname;
    private Label status = new Label("Ready");
    
    
    @Override
    public void start(Stage primaryStage) {
       
        
        fname ="Untitled";
        stage = primaryStage;
        
        
        
        BorderPane root = new BorderPane();
        root.setCenter(ta);
        
        
        GridPane menuPane = new GridPane();
        menuPane.add(menuBar(), 0, 0);
        menuPane.add(toolBar(), 0, 1);
        
        
        root.setTop(menuPane);
        root.setBottom(status);
        
        Scene scene = new Scene(root, 400, 300);
        
        primaryStage.setTitle(fname);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    private HBox toolBar()
    {
     HBox box = new HBox();
     
     String[] btns = {"New","Open","Save","Save As"};   
     
     for(int i =0;i<btns.length;i++)
     {
      Button b = new Button(btns[i]);    
      b.setOnAction(toolBarHandler);
      box.getChildren().add(b);
     }
     
     box.setSpacing(5);
     box.setStyle("-fx-background-color:#D3D3D3");
     return box;
        
       
    }
    
    private MenuBar menuBar()
    {
       Menu fileMenu = new Menu("File");
       
       Menu newMenuItem = new Menu("New");
       Menu openMenuItem = new Menu("Open");
       Menu saveMenuItem = new Menu("Save");
       Menu saveAsMenuItem = new Menu("Save As");
       Menu exitMenuItem = new Menu("Exit");
       
       
       newMenuItem.setOnAction(menuHandler);
       openMenuItem.setOnAction(menuHandler);
       saveMenuItem.setOnAction(menuHandler);
       saveAsMenuItem.setOnAction(menuHandler);
       
       exitMenuItem.setOnAction(actionEvent->Platform.exit());   
       
       fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, 
               saveAsMenuItem, new SeparatorMenuItem(), exitMenuItem);
        
       
       MenuBar bar = new MenuBar();
       
       bar.getMenus().add(fileMenu);
       
        return bar;
    }
    
    // Handler Methods
    public void handleEvent(String event)
    {
        switch(event)
        {
            case "New":
                    newFile();
                    break;
            case "Open":
                    openFile();
                    break;
            case "Save":
                    saveFile();
                    break;
            case "Save As":
                    saveFileAs();
                    break;
             
            
            
        }
        
        
    }
    
    EventHandler<ActionEvent> menuHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
        
         
        MenuItem mi = (MenuItem) event.getSource();
        handleEvent(mi.getText());  
        
    
        }
      };
    
     EventHandler<ActionEvent> toolBarHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event)
        {
            Button btn = (Button) event.getSource();
            handleEvent(btn.getText());
            
        }
         
         
         
         
     };

     private void newFile()
     {
       fname = "Untitled";
       stage.setTitle(fname);
       ta.setText("");
        
       ta.requestFocus();
         
     }
     
     private void saveFile()
     {
       if(fname.equals("Untitled"))
       {
           saveFileAs();
       }
       else
       {
          writeFile();   
       }  
         
     }
     
     private void openFile()
     {
        FileChooser fc = new FileChooser(); 
        
         FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JPad files (*.jpad)", "*.jpad"); 
         fc.getExtensionFilters().add(filter);
         
         File file = fc.showOpenDialog(stage);
         
         if(file==null)
         {
           status.setText("No File Selected");
           ta.requestFocus();
           
         }
         else
         {
          fname = file.getPath();
          readFile();
             
         }
     }
     
     
     private void saveFileAs()
     {
         FileChooser fc = new FileChooser(); 
        
         FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JPad files (*.jpad)", "*.jpad"); 
                 
         fc.getExtensionFilters().add(filter);
          
         File file = fc.showSaveDialog(stage);

         
           if(file==null)
         {
           status.setText("No Name Given");
           ta.requestFocus();
           
         }
         else
         {
          fname = file.getPath();
          writeFile();
             
         }
     }
     private void readFile()
     {
         if(fname.equals("Unititled"))
         {
             ta.setText("");
             ta.requestFocus();
         }
         else
         {
         FileInputStream fis = null;
           String s="";
            int ch;
         try
         {
            fis = new FileInputStream(fname); 
            while((ch= fis.read())!=-1)
            {
                s+= (char) ((ch+128)%256);
                
            }
            ta.setText(s);
            status.setText("File read Successfully");
            stage.setTitle("JPad - "+fname);
          
         }
         catch(Exception e)
         {
             status.setText("Error: "+e.getMessage());
         }
         finally
         {
             try
             {
                if(fis!=null)
                {
                   fis.close();
                }
             } catch (IOException ex) 
             {
                 status.setText("Error: "+ex.getMessage());
             }
         }
         
          
             
         }
         
     }
     private void writeFile()
     {
       FileOutputStream fout = null;
       
       try
       {
         fout = new FileOutputStream(fname);  
         
         String s = ta.getText();
         int ch = 0;
         
         for(int i=0;i<s.length();i++)   // when it's s.length()-1 it deletes the last thing enetered?
         {
           ch = s.charAt(i);
           fout.write((ch+128));
           
         }
         status.setText("File written successfully");
         stage.setTitle("JPad - "+fname);
       }
       catch (Exception e) 
       {
           status.setText("Error: "+e.getMessage());
       }
        finally
       {
         try
         {
          if(fout!=null)
          {
            fout.close();
          }
         }
         catch (IOException ex) 
         { 
           status.setText("Error:"+ex.getMessage());
         }       
                
              
       }
         
     }
     
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
