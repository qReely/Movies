package sample;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DataBase {
    public StringProperty title;
    public IntegerProperty budget;
    public StringProperty genres;
    public StringProperty homepage;
    public IntegerProperty id;
    public StringProperty keywords;
    public StringProperty original_language;
    public StringProperty original_title;
    public StringProperty overview;
    public DoubleProperty popularity;
    public StringProperty production_companies;
    public StringProperty production_countries;
    public StringProperty release_date;
    public LongProperty revenue;
    public IntegerProperty runtime;
    public StringProperty spoken_languages;
    public StringProperty status;
    public StringProperty tagline;
    public DoubleProperty vote_average;
    public IntegerProperty vote_count;
    public ImageView image;

    DataBase() {
    }

    public void setImage(ImageView image){
        this.image = image;
    }

    public ImageView getImage(){
        return image;
    }

    public void setBudget(int budget) {
        this.budget = new SimpleIntegerProperty(budget);
    }

    public void setGenres(String genres) {
        this.genres = new SimpleStringProperty(genres);
    }

    public void setHomepage(String homepage) {
        this.homepage = new SimpleStringProperty(homepage);
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public void setKeywords(String keywords) {
        this.keywords = new SimpleStringProperty(keywords);
    }

    public void setOriginal_language(String original_language) {
        this.original_language = new SimpleStringProperty(original_language);
    }

    public void setOriginal_title(String original_title) {
        this.original_title = new SimpleStringProperty(original_title);
    }

    public void setOverview(String overview) {
        this.overview = new SimpleStringProperty(overview);
    }

    public void setPopularity(double popularity) {
        this.popularity = new SimpleDoubleProperty(popularity);
    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    public void setProduction_companies(String production_companies) {
        this.production_companies = new SimpleStringProperty(production_companies);
    }

    public void setProduction_countries(String production_countries) {
        this.production_countries = new SimpleStringProperty(production_countries);
    }

    public void setRelease_date(String release_date) {
        this.release_date = new SimpleStringProperty(release_date);
    }

    public void setRevenue(long revenue) {
        this.revenue = new SimpleLongProperty(revenue);
    }

    public void setRuntime(int runtime) {
        this.runtime = new SimpleIntegerProperty(runtime);
    }

    public void setSpoken_languages(String spoken_languages) {
        this.spoken_languages = new SimpleStringProperty(spoken_languages);
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
    }

    public void setTagline(String tagline) {
        this.tagline = new SimpleStringProperty(tagline);
    }

    public void setVote_average(double vote_average) {
        this.vote_average = new SimpleDoubleProperty(vote_average);
    }

    public void setVote_count(int vote_count) {
        this.vote_count = new SimpleIntegerProperty(vote_count);
    }

    public DoubleProperty popularityProperty() {
        return popularity;
    }

    public DoubleProperty vote_averageProperty() {
        return vote_average;
    }

    public IntegerProperty budgetProperty() {
        return budget;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public LongProperty revenueProperty() {
        return revenue;
    }

    public IntegerProperty runtimeProperty() {
        return runtime;
    }

    public IntegerProperty vote_countProperty() {
        return vote_count;
    }

    public StringProperty genresProperty() {
        return genres;
    }

    public StringProperty homepageProperty() {
        return homepage;
    }

    public StringProperty keywordsProperty() {
        return keywords;
    }

    public StringProperty original_languageProperty() {
        return original_language;
    }

    public StringProperty original_titleProperty() {
        return original_title;
    }

    public StringProperty overviewProperty() {
        return overview;
    }

    public StringProperty production_companiesProperty() {
        return production_companies;
    }

    public StringProperty production_countriesProperty() {
        return production_countries;
    }

    public StringProperty release_dateProperty() {
        return release_date;
    }

    public StringProperty spoken_languagesProperty() {
        return spoken_languages;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty taglineProperty() {
        return tagline;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle(){
        if(title == null) return null;
        return title.get();
    }

    public int getBudget(){
        return budget.get();
    }

    public String getGenres(){
        if(genres == null) return null;
        return genres.get();
    }

    public String getHomepage(){
        if(homepage == null) return null;
        return homepage.get();
    }

    public int getId(){
        return id.get();
    }

    public String getKeywords(){
        if(keywords == null) return null;
        return keywords.get();
    }

    public String getOriginal_language(){
        if(original_language == null) return null;
        return original_language.get();
    }

    public String getOriginal_title(){
        if(original_title == null) return null;
        return original_title.get();
    }

    public String getOverview(){
        if(overview == null) return null;
        return overview.get();
    }

    public double getPopularity(){
        return popularity.get();
    }

    public String getProduction_companies(){
        if(production_companies == null) return null;
        return production_companies.get();
    }

    public String getProduction_countries(){
        if(production_countries == null) return null;
        return production_countries.get();
    }

    public String getRelease_date(){
        if(release_date == null) return null;
        return release_date.get();
    }

    public long getRevenue(){
        return revenue.get();
    }

    public int getRuntime(){
        return runtime.get();
    }

    public String getSpoken_languages(){
        if(spoken_languages == null) return null;
        return spoken_languages.get();
    }

    public String getStatus(){
        if(status == null) return null;
        return status.get();
    }

    public String getTagline(){
        if(tagline == null) return null;
        return tagline.get();
    }

    public double getVote_average(){
        return vote_average.get();
    }

    public int getVote_count(){
        return vote_count.get();
    }
}
