package gitlet;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String command = args[0];
//        System.out.println(command);
        if (command.equals("init")) {
            Execution.init();
        } else if (command.equals("add")) {
            Execution.add(args[1]);
        } else if (command.equals("commit")) {
            Execution.commit(args[1]);
        } else if (command.equals("rm")) {
            Execution.rm(args[1]);
        } else if (command.equals("log")) {
            Execution.log();
        } else if (command.equals("global-log")) {
            Execution.globalLog();
        } else if (command.equals("find")) {
            Execution.find(args[1]);
        } else if (command.equals("status")) {
            Execution.status();
        } else if (command.equals("checkout")) {
            if (args.length == 3) {
                if (!args[1].equals("--")) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Execution.checkout1(args[2]);
            } else if (args.length == 4) {
                if (!args[2].equals("--")) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Execution.checkout2(args[1], args[3]);
            } else {
                Execution.checkout3(args[1]);
            }
        } else if (command.equals("branch")) {
            Execution.branch(args[1]);
        } else if (command.equals("rm-branch")) {
            Execution.rmBranch(args[1]);
        } else if (command.equals("reset")) {
            Execution.reset(args[1]);
        } else if (command.equals("merge")) {
            Execution.merge(args[1]);
        } else {
            System.out.println("No command with that name exists.");
        }



    }


}
