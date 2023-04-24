module io.github.manuelosorio.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires javafx.base;

    opens io.github.manuelosorio.server to javafx.fxml, javafx.graphics, javafx.base;
    exports io.github.manuelosorio.server;
    exports io.github.manuelosorio;
    opens io.github.manuelosorio to javafx.base, javafx.fxml, javafx.graphics;
}