package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            JPanel translationPanel = new JPanel();
            translationPanel.add(new JLabel("Translation:"));
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            translationPanel.add(resultLabel);

            JPanel countryPanel = new JPanel();

            Translator translator = new JSONTranslator();

            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                LanguageCodeConverter converter = new LanguageCodeConverter();
                languageComboBox.addItem(converter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);

            String[] items = new String[translator.getCountryCodes().size()];
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                CountryCodeConverter converter1 = new CountryCodeConverter();
                items[i++] = converter1.fromCountryCode(countryCode);
            }

            JList<String> list = new JList<>(items);
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane, 0);

            // Unified update method
            Runnable updateTranslation = () -> {
                String selectedLanguage = (String) languageComboBox.getSelectedItem();
                String selectedCountry = list.getSelectedValue();

                // Convert names to codes
                String languageCode = null;
                for (String code : translator.getLanguageCodes()) {
                    LanguageCodeConverter converter = new LanguageCodeConverter();
                    if (converter.fromLanguageCode(code).equals(selectedLanguage)) {
                        languageCode = code;
                        break;
                    }
                }
                String countryCode = null;
                for (String code : translator.getCountryCodes()) {
                    CountryCodeConverter converter = new CountryCodeConverter();
                    if (converter.fromCountryCode(code).equals(selectedCountry)) {
                        countryCode = code;
                        break;
                    }
                }
                String answer = translator.translate(countryCode, languageCode);
                resultLabel.setText(answer);
            };

            languageComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateTranslation.run();
                    }
                }
            });

            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        updateTranslation.run();
                    }
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            // Update translation on startup if both selections are available
            if (languageComboBox.getItemCount() > 0 && list.getModel().getSize() > 0) {
                languageComboBox.setSelectedIndex(0);
                list.setSelectedIndex(0);
                updateTranslation.run();
            }
        });
    }
}
