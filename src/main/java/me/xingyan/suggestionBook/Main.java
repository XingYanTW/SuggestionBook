package me.xingyan.suggestionBook;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static spark.Spark.*;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("getbook").setExecutor(new cmdGetBook());
        getServer().getPluginManager().registerEvents(new eventBook(), this);

        //Create plugin folder if not exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        //save index.html and style.css at web folder to plugin folder + web folder if file not exist
        try {
            File webFolder = new File(getDataFolder(), "web");
            if (!webFolder.exists()) {
                webFolder.mkdir();
            }
            File index = new File(webFolder, "index.html");
            if (!index.exists()) {
                Files.copy(getResource("web/index.html"), index.toPath());
            }
            File assetFolder = new File(webFolder, "asset");
            if (!assetFolder.exists()) {
                assetFolder.mkdir();
            }
            File style = new File(assetFolder, "style.css");
            if (!style.exists()) {
                Files.copy(getResource("web/asset/style.css"), style.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create and .db file for saving suggestions
        try {
            File file = new File(getDataFolder(), "suggestions.db");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //connect to database using jdbc with suggestions.db
        try {
            Database.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //create table if not exist
        try {
            Database.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS suggestions (id INTEGER PRIMARY KEY AUTOINCREMENT, player TEXT, title, suggestion TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Run a website server for getting suggestions
        port(4567); // Set the port


        // Serve static files from the web folder
        // static file isn't include the index.html
        staticFiles.externalLocation(getDataFolder().getAbsolutePath() + "/web/asset/");

        // Handle the root endpoint
        get("/", (req, res) -> {
            File indexFile = new File(getDataFolder(), "web/index.html");
            String html = new String(Files.readAllBytes(indexFile.toPath()));

            // Get all suggestions from the database
            List<Suggestion> suggestions = new ArrayList<>();
            try {
                String sql = "SELECT * FROM suggestions";
                ResultSet rs = Database.getConnection().createStatement().executeQuery(sql);
                while (rs.next()) {
                    Suggestion suggestion = new Suggestion(
                            rs.getInt("id"),
                            rs.getString("player"),
                            rs.getString("title"),
                            rs.getString("suggestion")
                    );
                    suggestions.add(suggestion);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                res.status(500);
                return "Error retrieving suggestions";
            }

            // Build the suggestions HTML
            StringBuilder suggestionsHtml = new StringBuilder();
            for (Suggestion suggestion : suggestions) {
                suggestionsHtml.append("<div class=\"suggestion\">")
                        .append("<p class=\"title\">").append(suggestion.title).append("</p>")
                        .append("<hr>")
                        .append("<p>").append(suggestion.suggestion).append("</p>")
                        .append("<p class=\"player\"><span><img class=\"avatar\" src=\"https://mc-heads.net/avatar/")
                        .append(suggestion.player).append("\" alt=\"avatar\">").append(suggestion.player)
                        .append("</span></p>")
                        .append("</div>");
            }

            // Inject the suggestions HTML into the index.html content
            html = html.replace("<!-- suggestions -->", suggestionsHtml.toString());

            return html;
        });

        get("/suggestions", (req, res) -> {
            List<Suggestion> suggestions = new ArrayList<>();
            try {
                String sql = "SELECT * FROM suggestions";
                ResultSet rs = Database.getConnection().createStatement().executeQuery(sql);
                while (rs.next()) {
                    Suggestion suggestion = new Suggestion(
                            rs.getInt("id"),
                            rs.getString("player"),
                            rs.getString("title"),
                            rs.getString("suggestion")
                    );
                    suggestions.add(suggestion);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                res.status(500);
                return "Error retrieving suggestions";
            }
            res.type("application/json");
            return new Gson().toJson(suggestions);
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

            try {
                Database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    class Suggestion {
        private int id;
        private String player;
        private String title;
        private String suggestion;

        public Suggestion(int id, String player, String title, String suggestion) {
            this.id = id;
            this.player = player;
            this.title = title;
            this.suggestion = suggestion;
        }

        // Getters and setters (if needed)
    }
}
