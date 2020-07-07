package controller;

import builders.MonthsBuilder;
import data.ImportData;
import helpers.MovieTimeOfSeanceHelper;
import io.file.ConsolePrinter;
import model.Cinema;
import model.CinemaHall;
import model.Movie;
import model.Seance;
import org.apache.log4j.Logger;

import java.time.LocalTime;
import java.util.*;

public class MovieController {
    private final Logger logger = Logger.getLogger(MovieController.class);
    private final Scanner sc = new Scanner(System.in);
    public Cinema cinema;
    private ImportData importExport;


    public MovieController(ImportData importExport){
        this.importExport = importExport;
    }

    public MovieController() {
    }

    void addMovie() {
        try {
            Movie movie = addMovieToCinemaOffer();
            cinema.movies.put(movie.getTitle(), movie);
        } catch (InputMismatchException e) {
            System.out.println("Nie udało się utworzyć filmu. Niepoprawne dane!");
        }
    }

    private Movie addMovieToCinemaOffer() {
        logger.info("Tytuł filmu: ");
        String movieTitle = sc.nextLine();

        logger.info("Długość filmu w minutach: ");
        int movieLength = sc.nextInt();

//        logger.info("Ile razy w ciagu dnia bedzie wyświetlany film?");
//        List<LocalTime> movieDisplayHoursPerDay = MovieTimeOfSeanceHelper
//                .setHowManyTimesMovieWillBeShownDuringTheDay(sc.nextInt(), movieLength);
//
//        logger.info("Podaj nr sali kinowej: ");
//        int cinemaHallNumber = sc.nextInt();
//
//        logger.info("Podaj cenę ");
//        double price = sc.nextDouble();
//        sc.nextLine();

//        return Movie.Builder.newInstance()
//                .setTitle(movieTitle)
//                .setLength(movieLength)
//                .setMovieDisplayPerDay(movieDisplayHoursPerDay)
//                .setCinemaHallNumber(cinemaHallNumber)
//                .setPrice(price)
//                .build();
        return new Movie.Builder(movieTitle, movieLength).build();
    }

//    void addAdditionalDisplayTimeToSpecificMovie() {
//        try {
//            Seance movie = findSeanceByMovieTitle();
//
//            if (movie != null) {
//                List<LocalTime> movieDisplayPerDay = .getMovieDisplayPerDay();
//                movieDisplayPerDay.add(MovieTimeOfSeanceHelper.createTimeOfSeance());
//            }
//            else {
//                logger.info("Nie ma takiego filmu. Dostępne filmy: ");
//                printMovies();
//            }
//        } catch (NullPointerException e) {
//            logger.warn("Nie ma takiego filmu");
//        }
//    }

    Seance findSeanceByMovieTitle()
    {
        logger.info("Podaj tytuł filmu");
        String title = sc.nextLine();
        return cinema.seancesRepository.get(title);
    }
    Movie findMovieByTitle()
    {
        logger.info("Podaj tytuł filmu");
        String title = sc.nextLine();
        return cinema.movies.get(title);
    }

    void printMovies() {
        ConsolePrinter.printMovies(cinema.getMovies());
    }
    void printSeances() {
        ConsolePrinter.printSeances(cinema.getSeances());
    }


        void changeMoviePrice() {
            logger.info("Podaj tytuł filmu");
            String title = sc.nextLine();
            try {
                Seance seance = findSeanceByMovieTitle();
                if (seance.getMovie() != null) {
                    logger.info("Podaj nową cenę: ");
                    seance.setPrice(sc.nextDouble());
                    sc.nextLine();
                } else {
                    logger.warn("Nie ma takiego filmu. Dostępne filmy: ");
                    printMovies();
                }
            } catch (NullPointerException e) {
                logger.error("Nie ma takiego filmu");
            } catch (InputMismatchException e){
                logger.error("Niepoprawne dane!");
            }
        }

        void deleteMovie() {
            try {
                Movie movie = findMovieByTitle();

                if (removeMovie(movie))
                    logger.info("Usunięto film");
                else
                    logger.warn("Brak wskazanego filmu");
            } catch (InputMismatchException e) {
                logger.error("Nie udało się usunąć filmu, niepoprawne dane");
            }
        }
        boolean removeMovie(Movie movie){
            if (cinema.movies.containsValue(movie))
            {
            cinema.movies.remove(movie);
                return true;
            }else
                {
                return false;
            }
    }


//        int cinemaHallNumber = sc.nextInt();
//
//        logger.info("Podaj cenę ");
//        double price = sc.nextDouble();
//        sc.nextLine();

//        return Movie.Builder.newInstance()
//                .setTitle(movieTitle)
//                .setLength(movieLength)
//                .setMovieDisplayPerDay(movieDisplayHoursPerDay)
//                .setCinemaHallNumber(cinemaHallNumber)
//                .setPrice(price)
//                .build();
//private LocalDateTime dateTimeOfSeance;
//    private CinemaHall cinemaHall;
//    private Movie movie;
//    private double price;

    void addSeance() {
        try {
            Map<String, Seance> tempMap = addSeanceToCinemaOffer();
            cinema.seancesRepository.putAll(tempMap);
        } catch (InputMismatchException e) {
            System.out.println("Nie udało się utworzyć filmu. Niepoprawne dane!");
        }
    }



    public Map<String, Seance> addSeanceToCinemaOffer() {
        Map<String, Seance> tempMap = new HashMap<>();
        try {
            Movie movie = findMovieByTitle();
            if (movie != null){
                logger.info("Podaj miesiąc seansu: ");
                String inputMonth = sc.nextLine().toUpperCase();
                logger.info("Podaj dzień seansu: ");
                String inputDay = sc.nextLine().toUpperCase();
                logger.info("Podaj nr sali kinowej: ");
                int inputCinemaHallNumber = sc.nextInt();
                logger.info("Ile razy w ciagu dnia bedzie wyświetlany film?");
                List<LocalTime> movieDisplayHoursPerDay = MovieTimeOfSeanceHelper
                .setHowManyTimesMovieWillBeShownDuringTheDay(sc.nextInt(), movie.getLength());
                logger.info("Podaj cenę ");
                double price = sc.nextDouble();
                sc.nextLine();

                for (LocalTime localTime : movieDisplayHoursPerDay) {
                    CinemaHall cinemaHall = new CinemaHall.Builder(inputCinemaHallNumber).build();
                    Seance seance = new Seance.Builder()
                            .addDayOfSeance((MonthsBuilder.Month.valueOf(inputMonth)), MonthsBuilder.Week.valueOf(inputDay))
                            .setCinemaHall(cinemaHall)
                            .setDateTimeOfSeance(localTime)
                            .setMovie(movie)
                            .setPrice(price)
                            .build();
                    tempMap.put(movie.getTitle(), seance);
                }
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
        return tempMap;
    }
}
