module io.github.manuelosorio.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires javafx.base;

    opens io.github.manuelosorio.client to javafx.fxml, javafx.graphics, javafx.base;
    exports io.github.manuelosorio.client;
    exports io.github.manuelosorio;
    opens io.github.manuelosorio to javafx.base, javafx.fxml, javafx.graphics;
}