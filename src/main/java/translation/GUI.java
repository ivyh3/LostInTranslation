package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

            List<String> languageCodes = translator.getLanguageCodes();
            List<String> countryCodes = translator.getCountryCodes();

            // Basically replaces every element with the returned element from the inline function
            // like javascript's array.map((el) => new element) function
            // Can implement with for loop instead if you want
            List<String> languages = languageCodes.stream()
                    .map(langCode -> languageCodeConverter.fromLanguageCode(langCode)).toList();
            List<String> countries = countryCodes.stream()
                    .map((countryCode) -> countryCodeConverter.fromCountryCode(countryCode)).toList();

            
            // TODO: Remove dead code
            
             // JPanel countryPanel = new JPanel();
            // JTextField countryField = new JTextField(10);
            // countryField.setText("can");
            // countryField.setEditable(false); // we only support the "can" country code
            // for now
            // countryPanel.add(new JLabel("Country:"));
            // countryPanel.add(countryField);

            // Language selector panel
            JPanel languagePanel = new JPanel();
            JComboBox<String> languageComboBox = new JComboBox<>();
            for (String language : languages) {
                languageComboBox.addItem(language);
            }
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageComboBox);

            // Translation panel
            JPanel translationPanel = new JPanel();
            JLabel translationLabel = new JLabel("Translation:");
            JLabel translationText = new JLabel("\t\t\t\t\t\t\t");
            translationPanel.add(translationLabel);
            translationPanel.add(translationText);

            // Country List Panel
            JPanel countryListPanel = new JPanel();
            JList<String> countryList = new JList<String>(countries.toArray(new String[0]));
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane countryListContainer = new JScrollPane(countryList);
            countryListPanel.add(countryListContainer);

            // On country select, edit translation text label.
            countryList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String selectedCountry = countryList.getSelectedValue();
                    String selectedLanguage = languageComboBox.getSelectedItem().toString();

                    String translated = translator.translate(
                            countryCodeConverter.fromCountry(selectedCountry),
                            languageCodeConverter.fromLanguage(selectedLanguage));

                    translationText.setText(translated);

                    // TODO: Remove these later
                    System.out.println(selectedCountry + " | " + countryCodeConverter.fromCountry(selectedCountry));
                    System.out.println(selectedLanguage + " | " + languageCodeConverter.fromLanguage(selectedLanguage));
                    System.out.println(translated);
                }
            });

            // JPanel languagePanel = new JPanel();
            // JTextField languageField = new JTextField(10);
            // languagePanel.add(new JLabel("Language:"));
            // languagePanel.add(languageField);

            // JPanel buttonPanel = new JPanel();
            // JButton submit = new JButton("Submit");
            // buttonPanel.add(submit);

            // JLabel resultLabelText = new JLabel("Translation:");
            // buttonPanel.add(resultLabelText);
            // JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            // buttonPanel.add(resultLabel);

            // // adding listener for when the user clicks the submit button
            // submit.addActionListener(new ActionListener() {
            // @Override
            // public void actionPerformed(ActionEvent e) {
            // String language = languageField.getText();
            // String country = countryField.getText();

            // String result = translator.translate(country, language);
            // if (result == null) {
            // result = "no translation found!";
            // }
            // resultLabel.setText(result);

            // }

            // });

            // Build main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(countryListPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

        });
    }
}
