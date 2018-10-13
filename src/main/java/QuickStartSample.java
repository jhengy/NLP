// Imports the Google Cloud client library

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class QuickStartSample {
    /*
    getScore(): Returns an emotion score in the range of -1 to +1. a score less than 0 and up to -1
    implies a negative emotion and a score of more than 0 and up to +1 implies positive emotions.
    getMagnitude(): Magnitude is used to measure the amount of emotional content found in the text.
    Its value can range from 0 to infinity.

    * */
    public static void main(String... args) throws Exception {
        // Instantiates a client
        String test = "Love is fun";
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            /*// The text to analyze
            Document doc = Document.newBuilder().setContent(HELLO_WORLD).setType(Type.PLAIN_TEXT).build();
            // Detects the sentiment of the text
            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
            System.out.printf("Text: %s%n", HELLO_WORLD);
            System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());*/
            getSentiment(test);
        }
    }

    public static Sentiment getSentiment(String stringToProcess) {
        Sentiment sentiment = null;
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            // The text to analyze
            Document doc = Document.newBuilder().setContent(stringToProcess).setType(Type.PLAIN_TEXT).build();
            // Detects the sentiment of the text
            sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
            System.out.printf("Text: %s%n", stringToProcess);
            System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
        } catch (Exception e) {
            System.out.println("exception occurred at getSentiment()");
        }
        return sentiment;
    }

}
