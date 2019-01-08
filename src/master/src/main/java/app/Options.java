package app;

import picocli.CommandLine;

public class Options {
    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true,
            description = "Print usage help and exit.")
    public boolean usageHelpRequested;

    @CommandLine.Option(names = { "-c", "--config" }, description = "Config file path relative to the working directory or absolute.")
    public String config_ = "config.properties";

    @CommandLine.Option(names = { "-b", "--bind" }, description = "Bind IP address")
    public String bind_ = null;

    @CommandLine.Option(names = { "-wdc", "--without-discovery" }, description = "Disable discovery service")
    public boolean wdc_ = false;
}
