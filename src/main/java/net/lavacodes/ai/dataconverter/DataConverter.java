package net.lavacodes.ai.dataconverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Logger;

public class DataConverter {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        if(args.length > 0) {
            File directory = new File(args[0]);

            if(directory.exists()) {
                if (directory.isDirectory()) {
                    DataConverter converter = new DataConverter();
                    if(converter.beginConverting(directory)) {
                        logger.info("Successfully executed task.");
                        return;
                    }

                    logger.severe("Could not build file structure.");

                } else {
                    logger.severe("Provided file is no directory.");
                }
            } else {
                logger.severe("Provided directory does not exist.");
            }

            return;
        }
        logger.severe("No directory location provided.");
    }

    /**
     * It reads the list_name.txt file, creates a folder for each celebrity, and copies all of their images into their
     * respective folders
     *
     * @param directory The directory of the CelebA dataset.
     * @return A boolean value.
     */
    private boolean beginConverting(File directory) {
        File imagesDir = new File(directory.toString() + "/Images");
        try {
            String content = Files.readString(new File(directory + "/list_name.txt").toPath());
            String[] split = content.split("\n");

            File outputDir = new File(System.getProperty("user.dir") + "/Celebs");

            File[] imageSubs = imagesDir.listFiles(File::isDirectory);

            int counter = 0;
            if(outputDir.mkdir()) {
                for (String item : split) {
                    System.out.println("Fetching images of " + item + "...");

                    File outputCelebFolder = new File(outputDir + "/" + item);
                    if(outputCelebFolder.mkdir()) {

                        System.out.println(" -> Created output folder: " + outputCelebFolder.getAbsolutePath());
                        assert imageSubs != null;
                        File celebImageFolder = new File(imageSubs[counter] + "/frontal");

                        System.out.println(" -> Copying celebrity images... ");

                        int secondCount = 1;
                        for(File celebImage : Objects.requireNonNull(celebImageFolder.listFiles(File::isFile))) {
                            Files.copy(celebImage.toPath(), new File(outputCelebFolder + "/" + item + secondCount + ".png").toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println(" --> Copied " + celebImage.getAbsolutePath());

                            secondCount++;
                        }
                    } else {
                        return false;
                    }

                    counter++;
                }
            } else {
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


}
