import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhishingDetector extends JFrame {

    private JTextField urlField;
    private JLabel predictionLabel;
    private JLabel riskLabel;
    private JTextArea explanationArea;

    private Classifier model;
    private Instances structure;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PhishingDetector() {

        loadModel();
        createGUI();
    }


    // =====================================================
    // LOAD MODEL + DATASET
    // =====================================================

    private void loadModel() {

        try {

            model = (Classifier) weka.core.SerializationHelper.read(
                    "E:\\pwd pro\\phishing_48.model"
            );

            DataSource source = new DataSource(
                    "E:\\pwd pro\\Training Dataset.arff"
            );

            structure = source.getDataSet();

            structure.setClassIndex(
                    structure.numAttributes() - 1
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not load the ML model.\n\n"
                            + e.getMessage(),
                    "Model Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =====================================================
    // CREATE GUI
    // =====================================================

    private void createGUI() {

        setTitle(
                "Explainable AI - Phishing Website Detector"
        );

        setSize(800, 650);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);


        // =================================================
        // MAIN PANEL
        // =================================================

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );


        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel = new JLabel(
                "EXPLAINABLE AI-BASED PHISHING WEBSITE DETECTOR",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );


        // =================================================
        // CENTER PANEL
        // =================================================

        JPanel centerPanel = new JPanel();

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );


        // =================================================
        // URL LABEL
        // =================================================

        JLabel urlLabel = new JLabel(
                "Enter Website URL"
        );

        urlLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        centerPanel.add(urlLabel);

        centerPanel.add(
                Box.createVerticalStrut(8)
        );


        // =================================================
        // URL INPUT
        // =================================================

        urlField = new JTextField();

        urlField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        urlField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        centerPanel.add(urlField);

        centerPanel.add(
                Box.createVerticalStrut(12)
        );


        // =================================================
        // BUTTON
        // =================================================

        JButton checkButton = new JButton(
                "CHECK WEBSITE"
        );

        checkButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        checkButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        checkButton.addActionListener(
                e -> checkWebsite()
        );

        centerPanel.add(checkButton);

        centerPanel.add(
                Box.createVerticalStrut(20)
        );


        // =================================================
        // PREDICTION
        // =================================================

        predictionLabel = new JLabel(
                "Prediction: -",
                SwingConstants.CENTER
        );

        predictionLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        predictionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(predictionLabel);

        centerPanel.add(
                Box.createVerticalStrut(8)
        );


        // =================================================
        // RISK
        // =================================================

        riskLabel = new JLabel(
                "Risk Level: -",
                SwingConstants.CENTER
        );

        riskLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        riskLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(riskLabel);

        centerPanel.add(
                Box.createVerticalStrut(15)
        );


        // =================================================
        // EXPLANATION TITLE
        // =================================================

        JLabel explanationTitle = new JLabel(
                "Why did the system give this result?"
        );

        explanationTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        centerPanel.add(explanationTitle);

        centerPanel.add(
                Box.createVerticalStrut(5)
        );


        // =================================================
        // EXPLANATION AREA
        // =================================================

        explanationArea = new JTextArea();

        explanationArea.setEditable(false);

        explanationArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        explanationArea.setLineWrap(true);

        explanationArea.setWrapStyleWord(true);

        explanationArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        explanationArea
                );

        centerPanel.add(scrollPane);


        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        // =================================================
        // FOOTER
        // =================================================

        JLabel footer = new JLabel(
                "Machine Learning + Feature-Based Explainable Analysis",
                SwingConstants.CENTER
        );

        footer.setFont(
                new Font(
                        "Arial",
                        Font.ITALIC,
                        12
                )
        );

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );


        add(mainPanel);

        setVisible(true);
    }


    // =====================================================
    // CHECK WEBSITE
    // =====================================================

    private void checkWebsite() {

        String url =
                urlField.getText().trim();


        // -------------------------------------------------
        // INPUT VALIDATION
        // -------------------------------------------------

        if (url.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a website URL.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // Add protocol if user doesn't enter one
        if (!url.startsWith("http://")
                && !url.startsWith("https://")) {

            url = "http://" + url;
        }


        try {

            // =================================================
            // 1. EXTRACT FEATURES
            // =================================================

            double[] features =
                    URLFeatureExtractor.extractFeatures(url);


            // =================================================
            // 2. CREATE Weka INSTANCE
            // =================================================

            double[] values =
                    new double[
                            structure.numAttributes()
                            ];


            for (int i = 0;
                 i < features.length;
                 i++) {

                String featureValue =
                        String.valueOf(
                                (int) features[i]
                        );


                int nominalIndex =
                        structure.attribute(i)
                                .indexOfValue(
                                        featureValue
                                );


                /*
                 * Some attributes in your dataset
                 * don't contain every possible value.
                 */

                if (nominalIndex == -1) {

                    nominalIndex = 0;
                }


                values[i] =
                        nominalIndex;
            }


            // Class is unknown
            values[
                    structure.classIndex()
                    ] = Utils.missingValue();


            // =================================================
            // 3. CREATE INSTANCE
            // =================================================

            Instance instance =
                    new DenseInstance(
                            1.0,
                            values
                    );

            instance.setDataset(
                    structure
            );


            // =================================================
            // 4. ML PREDICTION
            // =================================================

            double prediction =
                    model.classifyInstance(
                            instance
                    );


            String result =
                    structure.classAttribute()
                            .value(
                                    (int) prediction
                            );


            // =================================================
            // 5. BUILD DYNAMIC XAI
            // =================================================

            List<String> warnings =
                    new ArrayList<>();

            List<String> positiveIndicators =
                    new ArrayList<>();


            // -----------------------------------------------
            // Feature 1: IP Address
            // -----------------------------------------------

            if (features[0] == 1) {

                warnings.add(
                        "The URL contains a direct IP address."
                );

            } else {

                positiveIndicators.add(
                        "No direct IP address detected."
                );
            }


            // -----------------------------------------------
            // Feature 2: URL Length
            // -----------------------------------------------

            if (features[1] == -1) {

                warnings.add(
                        "The URL is unusually long."
                );

            } else if (features[1] == 1) {

                positiveIndicators.add(
                        "The URL length is within a normal range."
                );
            }


            // -----------------------------------------------
            // Feature 3: URL Shortening
            // -----------------------------------------------

            if (features[2] == 1) {

                warnings.add(
                        "A URL shortening service is detected."
                );

            } else {

                positiveIndicators.add(
                        "No common URL shortening service detected."
                );
            }


            // -----------------------------------------------
            // Feature 4: @ Symbol
            // -----------------------------------------------

            if (features[3] == 1) {

                warnings.add(
                        "The URL contains an @ symbol."
                );

            } else {

                positiveIndicators.add(
                        "No @ symbol detected."
                );
            }


            // -----------------------------------------------
            // Feature 5: Double Slash
            // -----------------------------------------------

            if (features[4] == 1) {

                warnings.add(
                        "A suspicious double-slash redirect pattern is detected."
                );

            } else {

                positiveIndicators.add(
                        "No suspicious double-slash redirect detected."
                );
            }


            // -----------------------------------------------
            // Feature 6: Prefix/Suffix
            // -----------------------------------------------

            if (features[5] == 1) {

                warnings.add(
                        "The domain contains a suspicious hyphen pattern."
                );
            }


            // -----------------------------------------------
            // Feature 7: Subdomain
            // -----------------------------------------------

            if (features[6] == 1) {

                warnings.add(
                        "The URL contains multiple subdomains."
                );

            } else {

                positiveIndicators.add(
                        "Subdomain structure appears normal."
                );
            }


            // -----------------------------------------------
            // Feature 8: HTTPS
            // -----------------------------------------------

            if (features[7] == -1) {

                warnings.add(
                        "HTTPS is not detected."
                );

            } else {

                positiveIndicators.add(
                        "HTTPS is detected."
                );
            }


            // -----------------------------------------------
            // Feature 11: Port
            // -----------------------------------------------

            if (features[10] == 1) {

                warnings.add(
                        "A non-standard port is detected."
                );
            }


            // -----------------------------------------------
            // Feature 12: HTTPS Token
            // -----------------------------------------------

            if (features[11] == 1) {

                warnings.add(
                        "The HTTPS token appears in a suspicious position."
                );
            }


            // -----------------------------------------------
            // Feature 17: Email Submission
            // -----------------------------------------------

            if (features[16] == 1) {

                warnings.add(
                        "An email submission pattern is detected."
                );
            }


            // -----------------------------------------------
            // Feature 18: Abnormal URL
            // -----------------------------------------------

            if (features[17] == 1) {

                warnings.add(
                        "The URL has an abnormal structure."
                );
            }


            // =================================================
            // 6. RISK SCORE
            // =================================================

            int riskScore =
                    warnings.size();


            String riskLevel;


            if (riskScore <= 1) {

                riskLevel = "LOW";

            } else if (riskScore <= 3) {

                riskLevel = "MEDIUM";

            } else {

                riskLevel = "HIGH";
            }


            // =================================================
            // 7. DISPLAY ML RESULT
            // =================================================

            if (result.equals("-1")) {

                predictionLabel.setText(
                        "Prediction: LEGITIMATE"
                );

            } else if (result.equals("1")) {

                predictionLabel.setText(
                        "Prediction: PHISHING"
                );

            } else {

                predictionLabel.setText(
                        "Prediction: " + result
                );
            }


            // =================================================
            // 8. DISPLAY RISK
            // =================================================

            riskLabel.setText(
                    "Risk Level: "
                            + riskLevel
                            + "   |   Risk Indicators: "
                            + riskScore
            );


            // =================================================
            // 9. BUILD EXPLANATION TEXT
            // =================================================

            StringBuilder explanation =
                    new StringBuilder();


            explanation.append(
                    "ML MODEL RESULT\n"
            );

            explanation.append(
                    "The trained J48 model classified this URL as: "
            );

            if (result.equals("-1")) {

                explanation.append(
                        "LEGITIMATE\n\n"
                );

            } else if (result.equals("1")) {

                explanation.append(
                        "PHISHING\n\n"
                );

            } else {

                explanation.append(
                        result
                );

                explanation.append(
                        "\n\n"
                );
            }


            // =================================================
            // SUSPICIOUS FACTORS
            // =================================================

            if (!warnings.isEmpty()) {

                explanation.append(
                        "SUSPICIOUS FACTORS\n"
                );

                explanation.append(
                        "-------------------------\n"
                );


                for (String warning :
                        warnings) {

                    explanation.append(
                            "[!] "
                    );

                    explanation.append(
                            warning
                    );

                    explanation.append(
                            "\n"
                    );
                }


                explanation.append("\n");
            }


            // =================================================
            // POSITIVE INDICATORS
            // =================================================

            if (!positiveIndicators.isEmpty()) {

                explanation.append(
                        "POSITIVE INDICATORS\n"
                );

                explanation.append(
                        "-------------------------\n"
                );


                for (String indicator :
                        positiveIndicators) {

                    explanation.append(
                            "[OK] "
                    );

                    explanation.append(
                            indicator
                    );

                    explanation.append(
                            "\n"
                    );
                }
            }


            // =================================================
            // FINAL EXPLANATION
            // =================================================

            if (warnings.isEmpty()) {

                explanation.append(
                        "\nNo major suspicious URL characteristics "
                                + "were detected by the explanation layer."
                );
            }


            explanationArea.setText(
                    explanation.toString()
            );


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error while checking URL:\n\n"
                            + e.getMessage(),
                    "Prediction Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new PhishingDetector()
        );
    }
}