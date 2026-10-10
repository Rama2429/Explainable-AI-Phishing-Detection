import java.net.URL;

public class URLFeatureExtractor {

    // 1. having_IP_Address
    public static int hasIPAddress(String url) {
        try {
            URL u = new URL(url);
            String host = u.getHost();

            if (host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                return 1;
            }

            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    // 2. URL_Length
    public static int urlLength(String url) {
        int length = url.length();

        if (length < 54) {
            return 1;
        } else if (length <= 75) {
            return 0;
        } else {
            return -1;
        }
    }

    // 3. Shortining_Service
    public static int shorteningService(String url) {
        String[] shorteners = {
                "bit.ly", "tinyurl.com", "goo.gl", "t.co",
                "ow.ly", "is.gd", "buff.ly", "adf.ly",
                "cutt.ly", "shorturl.at"
        };

        String lowerUrl = url.toLowerCase();

        for (String shortener : shorteners) {
            if (lowerUrl.contains(shortener)) {
                return 1;
            }
        }

        return -1;
    }

    // 4. having_At_Symbol
    public static int hasAtSymbol(String url) {
        return url.contains("@") ? 1 : -1;
    }

    // 5. double_slash_redirecting
    public static int doubleSlashRedirecting(String url) {
        int index = url.indexOf("//", 8);
        return index != -1 ? 1 : -1;
    }

    // 6. Prefix_Suffix
    public static int prefixSuffix(String url) {
        try {
            URL u = new URL(url);
            String host = u.getHost();

            return host.contains("-") ? 1 : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    // 7. having_Sub_Domain
    public static int havingSubDomain(String url) {
        try {
            URL u = new URL(url);
            String host = u.getHost();

            String[] parts = host.split("\\.");

            if (parts.length <= 2) {
                return -1;
            } else if (parts.length == 3) {
                return 0;
            } else {
                return 1;
            }

        } catch (Exception e) {
            return -1;
        }
    }

    // 8. SSLfinal_State
    public static int sslFinalState(String url) {
        return url.toLowerCase().startsWith("https://") ? 1 : -1;
    }

    // 9. Domain_registeration_length
    public static int domainRegistrationLength(String url) {
        return 1;
    }

    // 10. Favicon
    public static int favicon(String url) {
        return -1;
    }

    // 11. port
    public static int port(String url) {
        try {
            URL u = new URL(url);

            if (u.getPort() == -1) {
                return -1;
            }

            return 1;

        } catch (Exception e) {
            return -1;
        }
    }

    // 12. HTTPS_token
    public static int httpsToken(String url) {
        String lower = url.toLowerCase();

        if (lower.contains("https") &&
                !lower.startsWith("https://")) {
            return 1;
        }

        return -1;
    }

    // 13. Request_URL
    public static int requestURL(String url) {
        return -1;
    }

    // 14. URL_of_Anchor
    public static int urlOfAnchor(String url) {
        return -1;
    }

    // 15. Links_in_tags
    public static int linksInTags(String url) {
        return -1;
    }

    // 16. SFH
    public static int sfh(String url) {
        return -1;
    }

    // 17. Submitting_to_email
    public static int submittingToEmail(String url) {
        return url.toLowerCase().contains("mailto:") ? 1 : -1;
    }

    // 18. Abnormal_URL
    public static int abnormalURL(String url) {
        return url.contains("@") ? 1 : -1;
    }

    // 19. Redirect
    public static int redirect(String url) {
        return -1;
    }

    // 20. on_mouseover
    public static int onMouseover(String url) {
        return -1;
    }

    // 21. RightClick
    public static int rightClick(String url) {
        return -1;
    }

    // 22. popUpWidnow
    public static int popUpWindow(String url) {
        return -1;
    }

    // 23. Iframe
    public static int iframe(String url) {
        return -1;
    }

    // 24. age_of_domain
    public static int ageOfDomain(String url) {
        return 1;
    }

    // 25. DNSRecord
    public static int dnsRecord(String url) {
        try {
            URL u = new URL(url);

            if (u.getHost() != null && !u.getHost().isEmpty()) {
                return 1;
            }

            return -1;

        } catch (Exception e) {
            return -1;
        }
    }

    // 26. web_traffic
    public static int webTraffic(String url) {
        return 0;
    }

    // 27. Page_Rank
    public static int pageRank(String url) {
        return 0;
    }

    // 28. Google_Index
    public static int googleIndex(String url) {
        return 1;
    }

    // 29. Links_pointing_to_page
    public static int linksPointingToPage(String url) {
        return 0;
    }

    // 30. Statistical_report
    public static int statisticalReport(String url) {
        return -1;
    }

    // Return all 30 features in the exact dataset order
    public static double[] extractFeatures(String url) {

        return new double[] {

                hasIPAddress(url),          // 1
                urlLength(url),             // 2
                shorteningService(url),     // 3
                hasAtSymbol(url),           // 4
                doubleSlashRedirecting(url),// 5
                prefixSuffix(url),          // 6
                havingSubDomain(url),       // 7
                sslFinalState(url),         // 8
                domainRegistrationLength(url), // 9
                favicon(url),               // 10
                port(url),                  // 11
                httpsToken(url),            // 12
                requestURL(url),            // 13
                urlOfAnchor(url),           // 14
                linksInTags(url),           // 15
                sfh(url),                   // 16
                submittingToEmail(url),     // 17
                abnormalURL(url),           // 18
                redirect(url),              // 19
                onMouseover(url),           // 20
                rightClick(url),            // 21
                popUpWindow(url),           // 22
                iframe(url),                // 23
                ageOfDomain(url),           // 24
                dnsRecord(url),             // 25
                webTraffic(url),            // 26
                pageRank(url),              // 27
                googleIndex(url),           // 28
                linksPointingToPage(url),   // 29
                statisticalReport(url)      // 30
        };
    }
}