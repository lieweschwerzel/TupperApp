package com.example.tupperapp.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class RecipeResponse {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("version")
        @Expose
        private Double version;
        @SerializedName("href")
        @Expose
        private String href;
        @SerializedName("results")
        @Expose
        private List<Recipe> results = null;

        /**
         * No args constructor for use in serialization
         *
         */
        public RecipeResponse() {
        }

        /**
         *
         * @param title
         * @param results
         * @param href
         * @param version
         */
        public RecipeResponse(String title, Double version, String href, List<Recipe> results) {
            super();
            this.title = title;
            this.version = version;
            this.href = href;
            this.results = results;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getVersion() {
            return version;
        }

        public void setVersion(Double version) {
            this.version = version;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public List<Recipe> getRecipes() {
            return results;
        }

        public void setResults(List<Recipe> results) {
            this.results = results;
        }

    }