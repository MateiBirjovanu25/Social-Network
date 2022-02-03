module com.example.map211psvm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.datatransfer;
    requires java.desktop;
    requires java.sql;
    requires org.apache.pdfbox;

    exports com.example.map211psvm;
    opens com.example.map211psvm to javafx.fxml;
    exports com.example.map211psvm.controller;
    opens com.example.map211psvm.controller to javafx.fxml;
    exports com.example.map211psvm.domain.dtos;
    opens com.example.map211psvm.domain.dtos to javafx.fxml;
    exports com.example.map211psvm.controller.request;
    opens com.example.map211psvm.controller.request to javafx.fxml;
    exports com.example.map211psvm.controller.event;
    opens com.example.map211psvm.controller.event to javafx.fxml;
    exports com.example.map211psvm.controller.page;
    opens com.example.map211psvm.controller.page to javafx.fxml;
    exports com.example.map211psvm.controller.notification;
    opens com.example.map211psvm.controller.notification to javafx.fxml;
}