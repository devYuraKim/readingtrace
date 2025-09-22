package com.yurakim.readingtrace.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponseDto {

    private List<BookItem> items;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BookItem {
        private String id;

        @JsonProperty("volumeInfo")
        private VolumeInfo volumeInfo;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private ImageLinks imageLinks;
        private String publisher;
        private String publishedDate;
        private String description;

        @JsonProperty("industryIdentifiers")
        private List<IndustryIdentifier> industryIdentifiers;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageLinks {
        private String thumbnail;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndustryIdentifier {
        private String type;
        private String identifier;
    }

}
