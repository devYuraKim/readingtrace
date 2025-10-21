package com.yurakim.readingtrace.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookDto {
    private String kind;
    private Integer totalItems;
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
        private String subtitle;
        private List<String> authors;
        private ImageLinks imageLinks;
        private String publisher;
        private String publishedDate;
        private String description;
        @JsonProperty("industryIdentifiers")
        private List<IndustryIdentifier> industryIdentifiers;
        private Integer pageCount;
        private Dimensions dimensions;
        private String printType;
        private String mainCategory;
        private List<String> categories;
        private Double averageRating;
        private Long ratingsCount;
        private String language;
        private String infoLink;
        private AccessInfo accessInfo;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageLinks {
        private String smallThumbnail;
        private String thumbnail;
        private String small;
        private String medium;
        private String large;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndustryIdentifier {
        private String type;
        private String identifier;
    }

    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dimensions {
        private String height;
        private String width;
        private String thickness;
    }

    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessInfo {
        private String country;
        private String viewability;
        private Boolean embeddable;
        private Boolean publicDomain;
        private String textToSpeechPermission;
        private Epub epub;
        private Pdf pdf;
        private String webReaderLink;
        private String accessViewStatus;
    }

    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Epub {
        private Boolean isAvailable;
        private String downloadLink;
        private String acsTokenLink;
    }

    @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pdf {
        private Boolean isAvailable;
        private String downloadLink;
        private String acsTokenLink;
    }


}
