package com.buildit;

import me.tongfei.progressbar.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class BuilditWebcrawler {

    private static Logger log = LoggerFactory.getLogger(BuilditWebcrawler.class);

    private HashSet<String> internalLinks;
    private HashSet<String> externalLinks;
    private HashMap<String, HashSet<String>> pageResources;
    private HashSet<String> brokenLinks;
    private int imgCount = 0;
    private URL crawlUrl;
    private ProgressBar pb;
    private SitemapGenerator gen;


    public BuilditWebcrawler(String url) {
        this.init(url);
    }

    private void init(String url) {
        if (url == null) {
            throw new IllegalArgumentException("Valid URL is required");
        }
        try {
            crawlUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error("Unable to initialize application with URL {} ", url, e.getMessage());
            System.exit(-1);
        }
        internalLinks = new HashSet<String>();
        externalLinks = new HashSet<String>();
        brokenLinks = new HashSet<String>();
        pageResources = new HashMap<String, HashSet<String>>();
        pb = new ProgressBar("Crawling " + crawlUrl.getHost(), 216);
        gen = new SitemapGenerator();
    }

    public Document connectToUrl(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public void getCurrentLinks(URL url) {
        if (!internalLinks.contains(url.toString()) && isLinkInDomain(url)) {
            HashSet<String> currentPageImages = new HashSet<String>();
            try {
                String stringUrl = url.toString();
                internalLinks.add(stringUrl);
                Document document = connectToUrl(stringUrl);
                pb.step();
                Elements imgSources = document.select("img");
                for (Element img : imgSources) {
                    currentPageImages.add(img.attr("abs:src"));
                    imgCount++;
                }

                pageResources.put(stringUrl, currentPageImages);
                Elements currentLinks = document.select("a[href]");
                for (Element page : currentLinks) {
                    if (isLinkInDomain(new URL(page.baseUri()))) {
                        getCurrentLinks(new URL(page.attr("abs:href")));
                    }
                }


            } catch (IOException e) {
                brokenLinks.add(url.toString());
            }
        } else if (!isLinkInDomain(url)) {
            externalLinks.add(url.toString());
        }
    }

    private void generateResult() {
        System.out.println("Internal Links:");
        gen.writeLineToFile("Internal Links:");
        pageResources.forEach((k, v) -> {
            System.out.println(k);
            gen.writeLineToFile(k);
            v.forEach((x) -> {
                System.out.println("\t"+x);
                gen.writeLineToFile("\t"+ x);
            });
        });
        System.out.println("External Links:");
        gen.writeLineToFile("External Links:");
        externalLinks.forEach((x) -> {
            System.out.println(x);
            gen.writeLineToFile(x);
        });
        try {
            gen.close();
        } catch (IOException e) {
            log.error("Unable to close stream", e.getMessage());
        }
        log.info("*** Total Visited Internal Links {} ***", internalLinks.size());
        log.info("*** Total External Links {} ***", externalLinks.size());
        log.info("*** Total Static Images Scanned {} ***", imgCount);
        log.info("*** Output File location: {} ***", gen.getOutputLoc());

    }

    private boolean isLinkInDomain(URL url) {
        return url.getHost().contains(crawlUrl.getHost());
    }

    public HashSet<String> getInternalLinks() {
        return internalLinks;
    }

    public void setInternalLinks(HashSet<String> internalLinks) {
        this.internalLinks = internalLinks;
    }

    public HashSet<String> getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(HashSet<String> externalLinks) {
        this.externalLinks = externalLinks;
    }

    public HashMap<String, HashSet<String>> getPageResources() {
        return pageResources;
    }

    public void setPageResources(HashMap<String, HashSet<String>> pageResources) {
        this.pageResources = pageResources;
    }

    public HashSet<String> getBrokenLinks() {
        return brokenLinks;
    }

    public void setBrokenLinks(HashSet<String> brokenLinks) {
        this.brokenLinks = brokenLinks;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public URL getCrawlUrl() {
        return crawlUrl;
    }

    public void setCrawlUrl(URL crawlUrl) {
        this.crawlUrl = crawlUrl;
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("Error, Missing URL !! Please provide a URL to docker-entrypoint.sh (Ex. http://google.com");
            System.exit(-1);
        }
        BuilditWebcrawler builditWebcrawler = new BuilditWebcrawler(args[0]);
        builditWebcrawler.pb.start();
        builditWebcrawler.getCurrentLinks(builditWebcrawler.crawlUrl);
        builditWebcrawler.pb.stop();
        builditWebcrawler.generateResult();
    }

}
