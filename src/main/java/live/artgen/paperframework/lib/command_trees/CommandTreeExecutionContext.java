package live.artgen.paperframework.lib.command_trees;

import java.util.ArrayList;

public record CommandTreeExecutionContext(String currentPath, String[] restPaths) {
}
