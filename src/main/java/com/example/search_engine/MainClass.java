package com.example.search_engine;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MainClass extends Application {
    static Database_Connection connect;

    @FXML
    private TextField text_field;

    @FXML
    private TextArea text_area;

    @FXML
    private Button search_button;

    @FXML
    private Button history_button;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("SearchPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Search Engine");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        connect = new Database_Connection();

//        ResultSet iterator = connect.executeQuery("select * from Search_History;");
//
//        while (iterator.next()){
//            String link_name = iterator.getString("link_name");
//            String link = iterator.getString("link");
//            System.out.println(link_name + " === " + link);
//        }

        launch();
    }

    public void SearchButtonClick(ActionEvent event) throws SQLException {
        // first, enter the keyword into the history table.
        String keyword = text_field.getText();
        System.out.println(keyword);
        Timestamp time = new Timestamp(System.currentTimeMillis());

        int response = connect.executeUpdate("Insert Into history Values ('" + keyword + "','" + time + "');");
        System.out.println(response);
        assert (response == 1);

        // now search for the related links in the Search_History table.
        String query = "Select link, link_name, time_stamp, (length(link) - length(replace(link, '" + keyword + "', '')))/length('" + keyword + "') as countofkeyword from Search_History order by countofkeyword desc limit 10";

        ResultSet iterator = connect.executeQuery(query);

        ArrayList<SearchResult> searchResults = new ArrayList<>();

        while(iterator.next()){
            String link = iterator.getString("link");
            String link_name = iterator.getString("link_name");
            Timestamp time1 = iterator.getTimestamp("time_stamp");
            int count = iterator.getInt("countofkeyword");

            searchResults.add(new SearchResult(link, link_name, time1));
        }

        StringBuilder show = new StringBuilder();
        String space = "     ";
        String nextline = "\n";

        for(SearchResult current: searchResults){
            show.append(current.getLink_name()).append(space).append(current.getLink()).append(space).append(current.getTime()).append(nextline);
        }

        text_area.setText(show.toString());

    }

    public void HistoryButtonClick(ActionEvent event) throws SQLException {
        // Show top 10 recently searched keywords.
        ResultSet iterator = connect.executeQuery("Select * from history order by TimeStamp desc limit 10;");

        ArrayList<HistoryResult> historyresults = new ArrayList<>();

        while (iterator.next()) {
            String keyword = iterator.getString("KeyWord");
            Timestamp time = iterator.getTimestamp("TimeStamp");

            System.out.println(keyword + "  ===  " + time);
            historyresults.add(new HistoryResult(keyword, time));
        }

        // let's display it in the text area

        StringBuilder show = new StringBuilder();
        String space = "                   ";
        String nextline = "\n";
        show.append("Keyword").append(space).append("Time").append(nextline).append(nextline);

        for (HistoryResult current : historyresults) {
            show.append(current.getKeyword()).append(space).append(current.getTimestamp()).append(nextline);
        }

        text_area.setText(show.toString());
    }
}

