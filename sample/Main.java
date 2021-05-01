package sample;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {

    final String table = "moviescopy";
    final String st1 = "SELECT * FROM " + table;
    final int chartRange = 10000000;
    final int lineRange = 1000000;
    final boolean isResizeable = true;
    TableView<DataBase> tableView = new TableView<>();
    ArrayList<TableColumn<DataBase, ?>> list = new ArrayList<>();
    ArrayList<String> columns = new ArrayList<>();
    ObservableList<DataBase> dataList;
    FilteredList<DataBase> filteredList;
    SortedList<DataBase> sortedData;
    Statement statement;
    Stage primaryStage;
    int previousId;
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis xAxisLine = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    NumberAxis yAxisLine = new NumberAxis();
    ObservableList<String> chartList = FXCollections.observableArrayList();
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    LineChart<Number,Number> lineChart = new LineChart<>(xAxisLine, yAxisLine);
    Pane searchAndSort;
    Connection connection;
    ComboBox<String> box = new ComboBox<>();
    ComboBox<String> box1 = new ComboBox<>();
    ObservableList<String> boxList = FXCollections.observableArrayList();
    TextField field = new TextField();

    @Override
    public void start(Stage primaryStage) {

        String url = "jdbc:oracle:thin:HR/hr@localhost:1521:xe";
        String name = "hr";
        String password = "hr";

        this.primaryStage = primaryStage;
        TextField toSearch = new TextField();
        Button delete = new Button("Delete");
        Button update = new Button("Update");
        Button create = new Button("Create");
        Button chart = new Button("Show Graph");
        Button exec = new Button("Execute");
        toSearch.setText("");
        boxList.addAll("Graph 1", "Graph 2");
        box.getItems().addAll(boxList);
        box1.getItems().addAll("top","popular");
        box1.setValue("top");
        addElements();

        tableView.setTableMenuButtonVisible(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn<DataBase, ?> dataTableColumn : list) {
            dataTableColumn.setPrefWidth(150);
            columns.add(dataTableColumn.getText());
            tableView.getColumns().add(dataTableColumn);
        }

        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(url);
            ods.setUser(name);
            ods.setPassword(password);
            connection = ods.getConnection();
            System.out.println("Connected");

            System.out.println("Hello!" + '\n' + "How does this program works?" + '\n' +
                    "There is a text field where you can search anything from database (it shows output immediately)" + '\n' +
                    "There are 5 buttons DELETE, UPDATE, CREATE, SHOW GRAPH, EXECUTE" + '\n' + "DELETE button deletes selected row" + '\n' +
                    "UPDATE button updates selected row" + '\n' + "CREATE button creates a new row in a database" + '\n' +
                    "SHOW GRAPH button shows selected graph from a drop-down menu" + '\n' + "EXECUTE button executes procedure selected from a drop-down menu with values entered in a text-field. Default value is 10");

            statement = connection.createStatement();
            dataList = getData();
            setImages(dataList);
            filteredList = new FilteredList<>(dataList, p -> true);

            // search
            toSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(data -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    // commented lines does not work correctly :(
                    if  (data.getGenres().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getTitle().toLowerCase().contains(lowerCaseFilter)) return true;
//                    else if(data.getHomepage().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getKeywords().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getOriginal_language().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getOriginal_title().toLowerCase().contains(lowerCaseFilter)) return true;
//                    else if(data.getOverview().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getProduction_companies().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getProduction_countries().toLowerCase().contains(lowerCaseFilter)) return true;
//                    else if(data.getRelease_date().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getSpoken_languages().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (data.getStatus().toLowerCase().contains(lowerCaseFilter)) return true;
//                    else if(data.getTagline().toLowerCase().contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getBudget()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getId()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getPopularity()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getRevenue()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getRuntime()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getVote_average()).contains(lowerCaseFilter)) return true;
                    else if (String.valueOf(data.getVote_count()).contains(lowerCaseFilter)) return true;
                    return false;
                });
            });

            delete.setOnAction(event -> {
                try {
                    if(tableView.isVisible())
                    handleDeleteData();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            create.setOnAction(event -> {
                try {
                    if(tableView.isVisible())
                    handleNewData(statement);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                }
            );

            update.setOnAction(event -> {
                try {
                    if(tableView.isVisible())
                    handleEditData(statement);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            exec.setOnAction(event -> {
                if(box1.getValue().equals("top"))
                    System.out.println("\nProcedure 'top':" + top(Integer.parseInt(field.getText())));
                else
                    System.out.println("\nProcedure 'popular':" + popular(Integer.parseInt(field.getText())));
            });

            chart.setOnAction(event -> {
                drawGraph();
                if(chart.getText().equals("Show Graph")) chart.setText("Show Table");
                else chart.setText("Show Graph");
            });


            sortedData = new SortedList<>(filteredList);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
            tableView.setPrefHeight(590);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Label label = new Label("Search:");
        Label procedures = new Label("Procedures:");
        label.setFont(new Font("Arial", 15));
        procedures.setFont(new Font("Arial", 15));
        field.relocate(230,80);
        field.setPrefColumnCount(5);
        field.setText("10");
        delete.relocate(420, 30);
        update.relocate(485, 30);
        create.relocate(555, 30);
        chart.relocate(525,80);
        exec.relocate(320,80);
        box.setValue("Graph 1");
        box1.relocate(130,80);
        box.relocate(420,80);
        label.relocate(30, 33);
        procedures.relocate(30,83);
        toSearch.setPrefColumnCount(25);
        toSearch.setPromptText("Search");
        toSearch.relocate(100, 30);
        searchAndSort = new Pane();
        searchAndSort.getChildren().addAll(toSearch, label, procedures, field, delete, update, create, chart,box,exec, box1);
        searchAndSort.setPadding(new Insets(25, 25, 0, 0));
        VBox root = new VBox(searchAndSort, tableView);
        primaryStage.setTitle("Movies");
        primaryStage.setScene(new Scene(root, 720, 720));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void drawGraph(){
        int max = 0;
        for(int i = 0; i < dataList.size(); i++){
            if(max < dataList.get(i).getBudget()) max = dataList.get(i).getBudget();
        }

        if(tableView.isVisible() && box.getValue().equals("Graph 1")){
            box.setVisible(false);
            ArrayList<String> temp = new ArrayList<>();
            int[] count = new int[max/chartRange];
            tableView.setVisible(false);
            for(int i = 0; i < dataList.size(); i++) {
                if(Math.floor(dataList.get(i).getBudget() / chartRange) >= 1)
                    count[(int) Math.floor(dataList.get(i).getBudget() / chartRange) - 1]++;
            }
            for(int i = 1; i < max/chartRange + 1; i++){
                temp.add(String.valueOf(i*chartRange/1000000));
            }
            chartList = FXCollections.observableArrayList(temp);
            xAxis.setCategories(chartList);
            xAxis.setLabel("Budget in millions of $");
            yAxis.setLabel("Numbers of movies");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (int i = 0; i < count.length; i++) {
                series.getData().add(new XYChart.Data<>(chartList.get(i), count[i]));
            }
            series.setName("Budget >= 10mil");
            barChart.getData().add(series);
            searchAndSort.getChildren().add(barChart);
            barChart.setVisible(true);
            barChart.relocate(0,110);
            barChart.setMinSize(600,600);
            barChart.setBarGap(1);
            barChart.setCategoryGap(5);
            barChart.setTitle("Movies Budget Chart");
        }
        else if(tableView.isVisible() && box.getValue().equals("Graph 2")){
            tableView.setVisible(false);
            box.setVisible(false);
            Number[] budget = new Number[max/lineRange + 1];
            Number[] temp = new Number[max/lineRange + 1];

            for(int i = 0; i < temp.length; i++){
                temp[i] = 0;
                budget[i] = 0;
            }

            for(int i = 0; i < dataList.size(); i++){
                if(dataList.get(i).getVote_count() > 50) {
                    if (temp[(int) Math.floor(dataList.get(i).getBudget() / lineRange)].equals(0))
                        temp[(int) Math.floor(dataList.get(i).getBudget() / lineRange)] = dataList.get(i).getVote_average();
                    else
                        temp[(int) Math.floor(dataList.get(i).getBudget() / lineRange)] = (dataList.get(i).getVote_average() + (double) temp[(int) Math.floor(dataList.get(i).getBudget() / lineRange)]) / 2;
                    if (budget[(int) Math.floor(dataList.get(i).getBudget() / lineRange)].equals(0))
                        budget[(int) Math.floor(dataList.get(i).getBudget() / lineRange)] = dataList.get(i).getBudget() / lineRange;
                    else
                        budget[(int) Math.floor(dataList.get(i).getBudget() / lineRange)] = (dataList.get(i).getBudget() / lineRange + (int) budget[(int) Math.floor(dataList.get(i).getBudget() / lineRange)]) / 2;
                }
            }

            int count = 1;
            for(int i = 0; i < temp.length; i++){
                if(!temp[i].equals(0)) count++;
            }

            yAxisLine.setLabel("Average votes");
            xAxisLine.setLabel("Average budget in millions of $");
            XYChart.Series<Number,Number> series = new XYChart.Series<>();
            for (int i = 0; i < temp.length; i++) {
                if(!temp[i].equals(0))
                    series.getData().add(new XYChart.Data<>(budget[i], temp[i]));
            }
            series.setName(count + " dots");
            lineChart.getData().addAll(series);
            lineChart.setVisible(true);
            lineChart.relocate(0,110);
            lineChart.setMinSize(600,600);
            lineChart.setTitle("Vote/Budget graph");
            searchAndSort.getChildren().add(lineChart);
        }
        else{
            box.setVisible(true);
            tableView.setVisible(true);
            searchAndSort.getChildren().removeAll(barChart,lineChart);
            barChart = new BarChart<>(xAxis, yAxis);
            lineChart = new LineChart<>(xAxisLine,yAxisLine);
            barChart.setVisible(false);
            lineChart.setVisible(false);
        }
    }


    public void setImages(ObservableList<DataBase> list) throws SQLException {
        ResultSet res = statement.executeQuery("Select id, image from " + table + " where image is not null");
        for(int i = 0; i < list.size(); i++){
            if(!res.next()) break;
            int id = res.getInt("id");
            Blob blob = res.getBlob("image");
            byte[] b = blob.getBytes(1, (int)blob.length());
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).getId() == id){
                    list.get(j).setImage(new ImageView(new Image(new ByteArrayInputStream(b))));
                    break;
                }
            }
        }
    }


    public ObservableList<DataBase> getData() throws SQLException {
        ObservableList<DataBase> temp = FXCollections.observableArrayList();
        ResultSet res = statement.executeQuery(st1);
        while (res.next()) {
            DataBase data = new DataBase();
            data.title = new SimpleStringProperty(res.getString("title"));
            data.budget = new SimpleIntegerProperty(res.getInt("budget"));
            data.genres = new SimpleStringProperty(remove(res.getString("genres")));
            data.homepage = new SimpleStringProperty(res.getString("homepage"));
            data.id = new SimpleIntegerProperty(res.getInt("id"));
            data.keywords = new SimpleStringProperty(remove(res.getString("keywords")));
            data.original_language = new SimpleStringProperty(res.getString("original_language"));
            data.original_title = new SimpleStringProperty(res.getString("original_title"));
            data.overview = new SimpleStringProperty(res.getString("overview"));
            data.popularity = new SimpleDoubleProperty(Double.parseDouble(res.getString("popularity").replace(',','.')));
            data.production_companies = new SimpleStringProperty(remove(res.getString("production_companies")));
            data.production_countries = new SimpleStringProperty(remove(res.getString("production_countries")));
            data.release_date = new SimpleStringProperty(res.getString("release_date"));
            data.revenue = new SimpleLongProperty(Long.parseLong(res.getString("revenue")));
            data.runtime = new SimpleIntegerProperty(res.getInt("runtime"));
            data.spoken_languages = new SimpleStringProperty(remove(res.getString("spoken_languages")));
            data.status = new SimpleStringProperty(res.getString("status"));
            data.tagline = new SimpleStringProperty(res.getString("tagline"));
            data.vote_average = new SimpleDoubleProperty(Double.parseDouble(res.getString("vote_average").replace(',','.')));
            data.vote_count = new SimpleIntegerProperty(res.getInt("vote_count"));
            temp.add(data);
        }
        return temp;
    }

    public String top(int top){
        try {
            String query = "begin top(?,?); end;";
            CallableStatement statement = connection.prepareCall(query);
            statement.registerOutParameter(2,OracleTypes.VARCHAR);
            statement.setInt(1,top);
            statement.execute();
            String res = statement.getString(2);
            return res;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public String popular(int top){
        try {
            String query = "begin popularFilms(?,?); end;";
            CallableStatement statement = connection.prepareCall(query);
            statement.registerOutParameter(2,OracleTypes.VARCHAR);
            statement.setInt(1,top);
            statement.execute();
            String res = statement.getString(2);
            return res;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


    public String remove(String toRemove) {
        StringBuilder sb = new StringBuilder();

        if (toRemove.contains("\\\"")) {
            toRemove = toRemove.replaceAll("\\\"", "");
        }

        sb.append(toRemove);

        if (sb.length() > 2) {
            if(sb.lastIndexOf("{") >= 0) {
                sb.delete(0, 1);
                sb.delete(sb.length() - 1, sb.length());
                if (sb.substring(2, 6).equals("name")) {
                    while (sb.lastIndexOf("{") >= 0) {
                        sb.delete(sb.lastIndexOf("\"") - 6, sb.lastIndexOf("}") + 1);
                        sb.delete(sb.lastIndexOf("{"), sb.lastIndexOf(":") + 3);
                    }
                } else {
                    while (sb.lastIndexOf("{") >= 0) {
                        sb.delete(sb.lastIndexOf("}") - 1, sb.lastIndexOf("}") + 1);
                        sb.delete(sb.lastIndexOf("{"), sb.lastIndexOf(":") + 3);
                    }
                }
            }

        }
        return sb.toString();

    }

    private void handleDeleteData() throws SQLException {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            statement.executeQuery("DELETE FROM " + table + " WHERE id = " + tableView.getSelectionModel().getSelectedItem().getId());
            dataList.removeAll(tableView.getSelectionModel().getSelectedItem());
            filteredList = new FilteredList<>(dataList, p -> true);
            sortedData = new SortedList<>(filteredList);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Row Selected");
            alert.setContentText("Please select a row in the table.");

            alert.showAndWait();
        }
    }

    private void handleNewData(Statement statement) throws IOException, SQLException {
        DataBase tempData = new DataBase();
        boolean okClicked = showDataEditDialog(tempData);
        if (okClicked) {
            statement.executeQuery("INSERT INTO " + table + " (title,budget,genres,homepage,id,keywords,original_language,original_title," +
                    "overview,popularity,production_companies,production_countries,release_date,revenue,runtime,spoken_languages,status," +
                    "tagline, vote_average, vote_count) VALUES('" + tempData.getTitle() + "','" + tempData.getBudget() + "','" + tempData.getGenres()
                    + "','" + tempData.getHomepage() + "','" + tempData.getId() + "','" + tempData.getKeywords() + "','" + tempData.getOriginal_language()
                    + "','" + tempData.getOriginal_title() + "','" + tempData.getOverview() + "','" + tempData.getPopularity() + "','" + tempData.getProduction_companies()
                    + "','" + tempData.getProduction_countries() + "','" + tempData.getRelease_date() + "','" + tempData.getRevenue() + "','" + tempData.getRuntime()
                    + "','" + tempData.getSpoken_languages() + "','" + tempData.getStatus() + "','" + tempData.getTagline() + "','" + tempData.getVote_average()
                    + "','" + tempData.getVote_count() + "')");
            dataList.add(tempData);
            filteredList = new FilteredList<>(dataList, p -> true);
            sortedData = new SortedList<>(filteredList);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
            tableView.refresh();
        }
    }

    @FXML
    private void handleEditData(Statement statement) throws IOException, SQLException {
        DataBase selectedData = tableView.getSelectionModel().getSelectedItem();
        if (selectedData != null) {
            boolean okClicked = showDataEditDialog(selectedData);
            if (okClicked) {
                statement.executeQuery("UPDATE " + table + " SET title='" + selectedData.getTitle() + "', budget=" + selectedData.getBudget() +
                        ", genres='" + selectedData.getGenres() + "', homepage ='" + selectedData.getHomepage() + "', id=" + selectedData.getId() +
                        ", keywords='" + selectedData.getKeywords() + "', original_language='" + selectedData.getOriginal_language() +
                        "', original_title='" + selectedData.getOriginal_title() + "', overview='" + selectedData.getOverview() + "', popularity =" +
                        selectedData.getPopularity() + ", release_date='" + selectedData.getRelease_date() + "', revenue=" + selectedData.getRevenue() +
                        ", runtime=" + selectedData.getRuntime() + ", spoken_languages='" + selectedData.getSpoken_languages() +"', status='" +
                        selectedData.getStatus() + "', tagline='" + selectedData.getTagline() + "', vote_average=" + selectedData.getVote_average() + ", vote_count=" +
                        selectedData.getVote_count() + " WHERE id=" + previousId);
                tableView.refresh();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Data Selected");
            alert.setContentText("Please select a data in the table.");

            alert.showAndWait();
        }
    }

    public void addElements() {
        TableColumn<DataBase, String> imageColumn = new TableColumn<>("image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        list.add(imageColumn);
        TableColumn<DataBase, String> titleColumn = new TableColumn<>("title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        list.add(titleColumn);
        TableColumn<DataBase, Integer> budgetColumn = new TableColumn<>("budget");
        budgetColumn.setCellValueFactory(cellData -> cellData.getValue().budgetProperty().asObject());
        list.add(budgetColumn);
        TableColumn<DataBase, String> genresColumn = new TableColumn<>("genres");
        genresColumn.setCellValueFactory(cellData -> cellData.getValue().genresProperty());
        list.add(genresColumn);
        TableColumn<DataBase, String> homepageColumn = new TableColumn<>("homepage");
        homepageColumn.setCellValueFactory(cellData -> cellData.getValue().homepageProperty());
        list.add(homepageColumn);
        TableColumn<DataBase, Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        list.add(idColumn);
        TableColumn<DataBase, String> keywordsColumn = new TableColumn<>("keywords");
        keywordsColumn.setCellValueFactory(cellData -> cellData.getValue().keywordsProperty());
        list.add(keywordsColumn);
        TableColumn<DataBase, String> originalLanguageColumn = new TableColumn<>("original_language");
        originalLanguageColumn.setCellValueFactory(cellData -> cellData.getValue().original_languageProperty());
        list.add(originalLanguageColumn);
        TableColumn<DataBase, String> originalTitleColumn = new TableColumn<>("original_title");
        originalTitleColumn.setCellValueFactory(cellData -> cellData.getValue().original_titleProperty());
        list.add(originalTitleColumn);
        TableColumn<DataBase, String> overviewColumn = new TableColumn<>("overview");
        overviewColumn.setCellValueFactory(cellData -> cellData.getValue().overviewProperty());
        list.add(overviewColumn);
        TableColumn<DataBase, Double> popularityColumn = new TableColumn<>("popularity");
        popularityColumn.setCellValueFactory(cellData -> cellData.getValue().popularityProperty().asObject());
        list.add(popularityColumn);
        TableColumn<DataBase, String> productionCompaniesColumn = new TableColumn<>("production_companies");
        productionCompaniesColumn.setCellValueFactory(cellData -> cellData.getValue().production_companiesProperty());
        list.add(productionCompaniesColumn);
        TableColumn<DataBase, String> productionCountriesColumn = new TableColumn<>("production_countries");
        productionCountriesColumn.setCellValueFactory(cellData -> cellData.getValue().production_countriesProperty());
        list.add(productionCountriesColumn);
        TableColumn<DataBase, String> releaseDateColumn = new TableColumn<>("release_date");
        releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().release_dateProperty());
        list.add(releaseDateColumn);
        TableColumn<DataBase, Long> revenueColumn = new TableColumn<>("revenue");
        revenueColumn.setCellValueFactory(cellData -> cellData.getValue().revenueProperty().asObject());
        list.add(revenueColumn);
        TableColumn<DataBase, Integer> runtimeColumn = new TableColumn<>("runtime");
        runtimeColumn.setCellValueFactory(cellData -> cellData.getValue().runtimeProperty().asObject());
        list.add(runtimeColumn);
        TableColumn<DataBase, String> spokenLanguagesColumn = new TableColumn<>("spoken_languages");
        spokenLanguagesColumn.setCellValueFactory(cellData -> cellData.getValue().spoken_languagesProperty());
        list.add(spokenLanguagesColumn);
        TableColumn<DataBase, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        list.add(statusColumn);
        TableColumn<DataBase, String> taglineColumn = new TableColumn<>("tagline");
        taglineColumn.setCellValueFactory(cellData -> cellData.getValue().taglineProperty());
        list.add(taglineColumn);
        TableColumn<DataBase, Double> voteAverageColumn = new TableColumn<>("vote_average");
        voteAverageColumn.setCellValueFactory(cellData -> cellData.getValue().vote_averageProperty().asObject());
        list.add(voteAverageColumn);
        TableColumn<DataBase, Integer> voteCountColumn = new TableColumn<>("vote_count");
        voteCountColumn.setCellValueFactory(cellData -> cellData.getValue().vote_countProperty().asObject());
        list.add(voteCountColumn);
    }

    public boolean showDataEditDialog(DataBase data) throws IOException {
        DataEditDialogController controller = new DataEditDialogController(data);

        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        loader.setLocation(Main.class.getResource("DataEditDialog.fxml"));
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Data");
        controller.setDataList(dataList);
        previousId = controller.getId();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        controller.setDialogStage(dialogStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return controller.isOkClicked();
    }

    public class DataEditDialogController {

        @FXML
        private TextField titleField = new TextField();
        @FXML
        private TextField budgetField = new TextField();
        @FXML
        private TextField genresField = new TextField("");
        @FXML
        private TextField homepageField = new TextField("");
        @FXML
        private TextField idField = new TextField("");
        @FXML
        private TextField keywordsField = new TextField("");
        @FXML
        private TextField originalLanguageField = new TextField("");
        @FXML
        private TextField originalTitleField = new TextField("");
        @FXML
        private TextField overviewField = new TextField("");
        @FXML
        private TextField popularityField = new TextField("");
        @FXML
        private TextField productionCompaniesField = new TextField("");
        @FXML
        private TextField productionCountriesField = new TextField("");
        @FXML
        private TextField releaseDateField = new TextField("");
        @FXML
        private TextField revenueField = new TextField("");
        @FXML
        private TextField runtimeField = new TextField("");
        @FXML
        private TextField spokenLanguagesField = new TextField("");
        @FXML
        private TextField statusField = new TextField("");
        @FXML
        private TextField taglineField = new TextField("");
        @FXML
        private TextField voteAverageField = new TextField("");
        @FXML
        private TextField voteCountField = new TextField("");

        Stage dialogStage;
        DataBase data;
        boolean okClicked = false;
        int previousID;
        ObservableList<DataBase> dataList;

        DataEditDialogController(DataBase data){
            if(data.getTitle() == null) {
                data.setBudget(0);
                data.setId(0);
                data.setPopularity(0);
                data.setRevenue(0);
                data.setRuntime(0);
                data.setVote_average(0);
                data.setVote_count(0);
                data.setRelease_date("01.01.21");
            }
            this.data = data;
            if(this.data.getRelease_date().length() > 8){
                String newDate = "";
                newDate += data.getRelease_date().charAt(8) + "" + data.getRelease_date().charAt(9) + '.';
                newDate += data.getRelease_date().charAt(5) +  "" + data.getRelease_date().charAt(6) + '.';
                newDate += data.getRelease_date().charAt(2) + "" + data.getRelease_date().charAt(3);
                this.data.setRelease_date(newDate);
            }
            previousID = data.getId();
        }

        public void setDataList(ObservableList<DataBase> dataList){
            this.dataList = dataList;
        }

        public int getId(){
            return previousID;
        }

        @FXML
        private void initialize() {
            titleField.setText(data.getTitle());
            budgetField.setText(Integer.toString(data.getBudget()));
            genresField.setText(data.getGenres());
            homepageField.setText(data.getHomepage());
            idField.setText(Integer.toString(data.getId()));
            keywordsField.setText(data.getKeywords());
            originalLanguageField.setText(data.getOriginal_language());
            originalTitleField.setText(data.getOriginal_title());
            overviewField.setText(data.getOverview());
            popularityField.setText(Double.toString(data.getPopularity()));
            productionCompaniesField.setText(data.getProduction_companies());
            productionCountriesField.setText(data.getProduction_countries());
            releaseDateField.setText(data.getRelease_date());
            revenueField.setText(Long.toString(data.getRevenue()));
            runtimeField.setText(Integer.toString(data.getRuntime()));
            spokenLanguagesField.setText(data.getSpoken_languages());
            statusField.setText(data.getStatus());
            taglineField.setText(data.getTagline());
            voteAverageField.setText(Double.toString(data.getVote_average()));
            voteCountField.setText(Integer.toString(data.getVote_count()));
        }

        public void setDialogStage(Stage dialogStage) {
            this.dialogStage = dialogStage;
        }

        public boolean isOkClicked() {
            return okClicked;
        }

        @FXML
        private void handleOk() throws IOException{
            if (isInputValid()) {
                data.setTitle(titleField.getText());
                data.setBudget(Integer.parseInt(budgetField.getText()));
                data.setGenres(genresField.getText());
                data.setHomepage(homepageField.getText());
                data.setId(Integer.parseInt(idField.getText()));
                data.setKeywords(keywordsField.getText());
                data.setOriginal_language(originalLanguageField.getText());
                data.setOriginal_title(originalTitleField.getText());
                data.setOverview(overviewField.getText());
                data.setPopularity(Double.parseDouble(popularityField.getText()));
                data.setProduction_companies(productionCompaniesField.getText());
                data.setProduction_countries(productionCountriesField.getText());
                data.setRelease_date(releaseDateField.getText());
                data.setRevenue(Long.parseLong(revenueField.getText()));
                data.setRuntime(Integer.parseInt(runtimeField.getText()));
                data.setSpoken_languages(spokenLanguagesField.getText());
                data.setStatus(statusField.getText());
                data.setTagline(taglineField.getText());
                data.setVote_average(Double.parseDouble(voteAverageField.getText()));
                data.setVote_count(Integer.parseInt(voteCountField.getText()));

                okClicked = true;
                final Stage stageToClose = dialogStage;
                stageToClose.close();
            }
        }

        @FXML
        private void handleCancel() throws IOException{
            dialogStage.close();
        }

        private boolean isInputValid() {
            String errorMessage = "";

            if (titleField.getText() == null || titleField.getText().length() == 0) {
                errorMessage += "No valid title!\n";
            }

            if (budgetField.getText() == null || budgetField.getText().length() == 0) {
                errorMessage += "No valid budget!\n";
            } else {
                try {
                    Integer.parseInt(budgetField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid budget (must be an integer)!\n";
                }
            }

            if (idField.getText() == null || idField.getText().length() == 0) {
                errorMessage += "No valid id!\n";
            } else {
                try {
                    int id = Integer.parseInt(idField.getText());
                    if(id < 1) errorMessage += "Id must be a positive, non-zero number\n";
                    else{
                        for(int i = 0; i < dataList.size(); i++){
                            if(dataList.get(i).getId() == Integer.parseInt(idField.getText()) && dataList.get(i).getId() != previousID){
                                errorMessage += "Id must be unique\n";
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    errorMessage += "No valid id (must be an integer)!\n";
                }

            }

            if (originalTitleField.getText() == null || originalTitleField.getText().length() == 0) {
                errorMessage += "No valid original title!\n";
            }

            if (popularityField.getText() == null || popularityField.getText().length() == 0) {
                errorMessage += "No valid popularity!\n";
            } else {
                try {
                    popularityField.setText(String.valueOf(Double.parseDouble(popularityField.getText())));
                } catch (NumberFormatException e) {
                    errorMessage += "No valid popularity (must be a double)!\n";
                }
            }

            if (releaseDateField.getText() == null || releaseDateField.getText().length() == 0) {
                errorMessage += "No valid release date!\n";
            } else {
                String date = releaseDateField.getText();
                if (date.length() != 8 || date.charAt(2) != '.' || date.charAt(5) != '.')
                    errorMessage += "Release date should be in that form: dd.mm.yy\n";
                else {
                    int day = Integer.parseInt(date.charAt(0) + "" + date.charAt(1));
                    int month = Integer.parseInt(date.charAt(3) + "" + date.charAt(4));
                    if (day > 31) errorMessage += "Bad value for a day\n";
                    else if (month > 12) errorMessage += "Bad value for a month\n";
                }
            }

            if (revenueField.getText() == null || revenueField.getText().length() == 0) {
                errorMessage += "No valid revenue!\n";
            } else {
                try {
                    Long.parseLong(revenueField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid revenue (must be an integer)!\n";
                }
            }

            if (runtimeField.getText() == null || runtimeField.getText().length() == 0) {
                errorMessage += "No valid runtime!\n";
            } else {
                try {
                    Integer.parseInt(runtimeField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid runtime (must be an integer)!\n";
                }
            }

            if (voteAverageField.getText() == null || voteAverageField.getText().length() == 0) {
                errorMessage += "No valid average vote!\n";
            } else {
                try {
                    voteAverageField.setText(String.valueOf(Double.parseDouble(voteAverageField.getText())));
                } catch (NumberFormatException e) {
                    errorMessage += "No valid average vote (must be a double)!\n";
                }
            }

            if (voteCountField.getText() == null || voteCountField.getText().length() == 0) {
                errorMessage += "No valid vote count!\n";
            } else {
                try {
                    Double.parseDouble(voteAverageField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid vote count (must be an integer)!\n";
                }
            }

            if (errorMessage.length() == 0) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText(errorMessage);

                alert.showAndWait();

                return false;
            }
        }
    }
}