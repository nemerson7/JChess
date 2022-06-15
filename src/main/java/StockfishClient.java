import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.UnaryOperator.identity;

public class StockfishClient {

    private Process proc = null;
    private BufferedReader reader = null;
    private OutputStreamWriter writer = null;
    private BoardPanel panelRef;

    public StockfishClient(BoardPanel panel) {
        this.panelRef = panel;
    }

    public void initialize() {

        var procBuilder = new ProcessBuilder("stockfish");
        try {
            this.proc = procBuilder.start();
            this.reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            this.writer = new OutputStreamWriter(proc.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("started stockfish process\n");
    }

    public void exitClient() {

        if (this.proc.isAlive()) {
            this.proc.destroy();
        }
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("exited stockfish process\n");
    }

    //taken from this cool blog post:
    // https://www.andreinc.net/2021/04/22/writing-a-universal-chess-interface-client-in-java
    public <T> T command(String cmd, Function<List<String>, T> commandProcessor, Predicate<String> breakCondition, long timeout)
            throws InterruptedException, ExecutionException, TimeoutException {

        // This completable future will send a command to the process
        // And gather all the output of the engine in the List<String>
        // At the end, the List<String> is translated to T through the
        // commandProcessor Function
        CompletableFuture<T> command = supplyAsync(() -> {
            final List<String> output = new ArrayList<>();
            try {
                writer.flush();
                writer.write(cmd + "\n");
                writer.write("isready\n");
                writer.flush();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Unknown command")) {
                        throw new RuntimeException(line);
                    }
                    if (line.contains("Unexpected token")) {
                        throw new RuntimeException("Unexpected token: " + line);
                    }
                    output.add(line);
                    if (breakCondition.test(line)) {
                        // At this point we are no longer interested to read any more
                        // output from the engine, we consider that the engine responded
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return commandProcessor.apply(output);
        });

        return command.get(timeout, TimeUnit.MILLISECONDS);
    }


    //returns array of [currentPos, destinationPos]
    public Square[] computerMove() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        this.command("uci", identity(), (s) -> s.startsWith("uciok"), 2000l);
        this.command("position fen " + this.panelRef.parseBoardToFEN(), identity(), s -> s.startsWith("readyok"), 2000l);

        String bestMove = this.command(
                        "go movetime 1000",
                        lines -> lines.stream().filter(s->s.startsWith("bestmove")).findFirst().get(),
                        line -> line.startsWith("bestmove"),
                        5000l)
                .split(" ")[1];
        System.out.println(bestMove);

        //parsing bestMove into a square on board
        return this.parseNotationStringToSquare(bestMove);
    }

    public Square[] parseNotationStringToSquare(String posString) {
        String part1 = posString.substring(0, 2);
        String part2 = posString.substring(2);

        return new Square[]{this.singleNotationCoordToSquare(part1), this.singleNotationCoordToSquare(part2)};
    }

    public Square singleNotationCoordToSquare(String posArr) {
        char[] arr = posArr.toCharArray();

        System.out.println(((int) arr[0]) - 97);
        System.out.println(Math.abs(Integer.parseInt(String.valueOf(arr[1])) - 7));
        return this.panelRef.board[Math.abs(Integer.parseInt(String.valueOf(arr[1])) - 8)][((int) arr[0]) - 97];
    }
}
