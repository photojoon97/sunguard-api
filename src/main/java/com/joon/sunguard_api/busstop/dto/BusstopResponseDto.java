package com.joon.sunguard_api.busstop.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "response")
public class BusstopResponseDto {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Getter
    @Setter
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {

        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;

        @JacksonXmlElementWrapper(localName = "items")
        @JacksonXmlProperty(localName = "item")
        private List<Item> items;
    }

    @Getter
    @Setter
    public static class Item {

        @JacksonXmlProperty(localName = "arsno")
        private String arsno;

        @JacksonXmlProperty(localName = "bstopid")
        private String bstopid;

        @JacksonXmlProperty(localName = "bstopnm")
        private String bstopnm;

        @JacksonXmlProperty(localName = "gpsx")
        private double gpsx;

        @JacksonXmlProperty(localName = "gpsy")
        private double gpsy;

        @JacksonXmlProperty(localName = "stoptype")
        private String stoptype;
    }
}
