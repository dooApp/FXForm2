package com.dooapp.fxform2.fxreport;

import com.dooapp.fxform.FXForm;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;

public class Test extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        InputStream in = Test.class.getResourceAsStream("/test.odt");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

        FXForm fxForm = new FXForm();
        ReportElementProvider reportElementProvider = new ReportElementProvider(report);
        fxForm.setElementProvider(reportElementProvider);
        fxForm.setSource(report);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(fxForm);
        Button button = new Button("Generate report");
        button.setOnAction(event -> {
            try {
                IContext context = report.createContext();
                reportElementProvider.fillContext(context);
                File file = new File("test_out.odt");
                OutputStream out = new FileOutputStream(file);
                report.process(context, out);
                Desktop.getDesktop().open(file);
            } catch (XDocReportException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        borderPane.setBottom(button);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
