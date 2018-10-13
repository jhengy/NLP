import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.util.Map;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {

    public static void main(String[] args)  {
        try {
            //getSentiment("idea");
            //analyzeEntitiesText("linkedlist");
            analyseEntitiesSentimentText("British is a nice place blockchain");

        } catch (Exception e) {
            System.out.println("caught in main");
        }
    }

    public static Sentiment getSentiment(String message) throws Exception {
        Sentiment sentiment = null;
        // Instantiates a client
        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            // The text to analyze

            Document doc = Document.newBuilder()
                    .setContent(message).setType(Type.PLAIN_TEXT).build();

            // Detects the sentiment of the text
            sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

            System.out.printf("Text: %s%n", message);
            System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
        }

        return sentiment;
    }


    public static void analyseEntitiesSentimentText(String text) throws Exception {
        // [START language_entity_sentiment_text]
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text).setType(Type.PLAIN_TEXT).build();
            // request encapsulates the input text we want to analyse on
            AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16).build();
            // detect entity sentiments in the given string
            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.printf("Sentiment : %s\n", entity.getSentiment());

                // extracting wikipedia
                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                    System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
                    //System.out.println(entry.getKey().equals("wikipedia_url"));
                }

                for (EntityMention mention : entity.getMentionsList()) {
                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
                    System.out.printf("Content: %s\n", mention.getText().getContent());
                    System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
                    System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
                    System.out.printf("Type: %s\n\n", mention.getType());
                }
            }
        }
        // [END language_entity_sentiment_text]
    }


    /*
    salience indicates the importance or relevance of this entity to the entire document text.
    This score can assist information retrieval and summarization by prioritizing salient entities.
    Scores closer to 0.0 are less important, while scores closer to 1.0 are highly important.
     */
    public static void analyzeEntitiesText(String text) throws Exception {
        // [START language_entities_text]
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Type.PLAIN_TEXT)
                    .build();
            AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            AnalyzeEntitiesResponse response = language.analyzeEntities(request);

            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.println("Metadata: ");
                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                    System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
                }
                for (EntityMention mention : entity.getMentionsList()) {
                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
                    System.out.printf("Content: %s\n", mention.getText().getContent());
                    System.out.printf("Type: %s\n\n", mention.getType());
                }
            }
        }
        // [END language_entities_text]
    }




    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            long chat_id = update.getMessage().getChatId();
            update.getMessage().getFrom().getUserName()
            Sentiment sentiment = null;
            try {
                //sentiment = getSentiment(update.getMessage().getText());


            } catch (Exception e) {
                System.err.print(e);
            }

            /*
            if (sentiment == null) {
                executeSendMessage("can't analyze the sentiment.", chat_id);
                return;
            } */
            // Set variables
            //String responseMessage = "Sentiment: " + sentiment.getScore() + ", " + sentiment.getMagnitude();

            executeSendMessage(update.toString(), chat_id);
        }
    }

    // send out a message to the chat group with chat_id.
    public void executeSendMessage(String message, Long chat_id) {
        SendMessage send = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(message);
        try {
            execute(send); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "hOverflow_bot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "602067071:AAH2HU94g50AmTDIJSJS3Il96j8WIVtv4bA";
    }
}