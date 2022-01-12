import controller.ReservedBookDetailsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ReservedBookDetails;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, ParseException, ClassNotFoundException {
        deleteExpiredReservations();
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/MainInterfaceForm.fxml"))));
        primaryStage.setX(250);
        primaryStage.setY(50);
        primaryStage.centerOnScreen();
        primaryStage.show();

    }

    public void deleteExpiredReservations() throws ParseException, SQLException, ClassNotFoundException {
        for (ReservedBookDetails reservation : new ReservedBookDetailsController().getAllReservations()) {
            if (reservation.getStatus().equals("Available")){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date availableTill = format.parse(reservation.getAvailableTill());

                Date currentDate =new Date();

                long difference = currentDate.getTime() - availableTill.getTime();
                TimeUnit time = TimeUnit.DAYS;
                long differenceOfDays = time.convert(difference, TimeUnit.MILLISECONDS);
                System.out.println(differenceOfDays);
                if (differenceOfDays>0){
                    System.out.println("In");
                    new ReservedBookDetailsController().deleteReservation(reservation);
                }

            }
        }
    }
}
