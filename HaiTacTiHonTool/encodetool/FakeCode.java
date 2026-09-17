package encodetool;

import javassist.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.stream.Stream;

public final class FakeCode {

    private static final SecureRandom RAND = new SecureRandom();
    private static int nameCounter = 0;

    private static final String[] GAME_NOUNS = {
            "Player", "Map", "Monster", "NPC", "Item", "Skill", "Network",
            "Session", "Game", "UI", "Sprite", "Effect", "Camera", "World",
            "Entity", "Character", "Weapon", "Fashion", "Config", "Data",
            "Inventory", "Quest", "Task", "Shop", "Trade", "Party", "Guild",
            "Chat", "Event", "Arena", "Boss", "Match", "Server", "Client",
            "Database", "Cache", "Log", "System", "Engine", "Physics",
            "Render", "Audio", "Input", "State", "Manager", "Controller",
            "Handler", "Provider", "Service", "Factory", "Builder", "Context",
            "Info", "Stats", "Profile", "Account", "Login", "Auth", "Security"
    };

    private static final String[] GAME_VERBS = {
            "update", "process", "render", "load", "save", "init", "start",
            "stop", "destroy", "create", "delete", "add", "remove", "get",
            "set", "find", "search", "check", "validate", "send", "receive",
            "handle", "execute", "run", "calculate", "apply", "revert",
            "reset", "clear", "build", "parse", "format", "sync", "connect",
            "disconnect", "read", "write", "encrypt", "decrypt", "compress"
    };

    private static final String[] TYPES = {"int", "long", "double", "float", "boolean", "String"};

    public static void main(String[] args) throws Exception {
        String classesDir = args.length > 0 ? args[0] : "target/classes";
        int complexity = args.length > 1 ? Integer.parseInt(args[1]) : 5;

        System.out.println("[*] Initializing Javassist Bytecode Injector...");
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(classesDir);

        Path rootPath = Paths.get(classesDir);
        
        if (!Files.exists(rootPath)) {
            System.err.println("Classes directory not found: " + classesDir);
            return;
        }

        int injectedClasses = 0;
        int totalMethods = 0;

        try (Stream<Path> paths = Files.walk(rootPath)) {
            for (Path path : (Iterable<Path>) paths::iterator) {
                if (Files.isRegularFile(path) && path.toString().endsWith(".class") && !path.toString().contains("encodetool")) {
                    String className = rootPath.relativize(path).toString()
                            .replace(File.separatorChar, '.')
                            .replace(".class", "");

                    try {
                        CtClass cc = pool.get(className);
                        if (cc.isInterface() || cc.isEnum() || cc.isAnnotation()) continue;

                        int injectCount = 5 + RAND.nextInt(complexity * 3);
                        for (int i = 0; i < injectCount; i++) {
                            injectFakeMethod(cc);
                            totalMethods++;
                        }

                        int fieldCount = 2 + RAND.nextInt(complexity);
                        for (int i = 0; i < fieldCount; i++) {
                            injectFakeField(cc);
                        }

                        cc.writeFile(classesDir);
                        cc.detach();
                        injectedClasses++;
                    } catch (Exception e) {
                        System.err.println("Skipping class " + className + ": " + e.getMessage());
                    }
                }
            }
        }

        System.out.println("[+] Injected " + totalMethods + " fake logic routines into " + injectedClasses + " real classes!");
    }

    private static void injectFakeField(CtClass cc) throws CannotCompileException {
        String type = TYPES[RAND.nextInt(TYPES.length)];
        String name = randomLocalName();
        String init = obfuscatedValue(type);
        CtField f = CtField.make("private " + type + " " + name + " = " + init + ";", cc);
        cc.addField(f);
    }

    private static void injectFakeMethod(CtClass cc) throws CannotCompileException {
        String ret = TYPES[RAND.nextInt(TYPES.length)];
        String name = randomMethodName();

        StringBuilder body = new StringBuilder();
        body.append("public ").append(ret).append(" ").append(name).append("() {\n");
        
        int lines = 3 + RAND.nextInt(5);
        for (int i = 0; i < lines; i++) {
            String type = TYPES[RAND.nextInt(TYPES.length)];
            String varName = randomLocalName();
            body.append("    ").append(type).append(" ").append(varName).append(" = ").append(obfuscatedValue(type)).append(";\n");
        }

        body.append("    return ").append(obfuscatedValue(ret)).append(";\n");
        body.append("}\n");

        CtMethod m = CtNewMethod.make(body.toString(), cc);
        cc.addMethod(m);
    }

    private static String randomMethodName() {
        return GAME_VERBS[RAND.nextInt(GAME_VERBS.length)] + 
               GAME_NOUNS[RAND.nextInt(GAME_NOUNS.length)] + 
               (RAND.nextBoolean() ? GAME_NOUNS[RAND.nextInt(GAME_NOUNS.length)] : "") + (nameCounter++);
    }

    private static String randomLocalName() {
        String noun = GAME_NOUNS[RAND.nextInt(GAME_NOUNS.length)];
        return Character.toLowerCase(noun.charAt(0)) + noun.substring(1) + (nameCounter++);
    }

    private static String obfuscatedValue(String type) {
        switch (type) {
            case "int": return String.valueOf(RAND.nextInt());
            case "long": return RAND.nextLong() + "L";
            case "double": return RAND.nextDouble() + "d";
            case "float": return RAND.nextFloat() + "f";
            case "boolean": return RAND.nextBoolean() ? "true" : "false";
            default: return "\"" + GAME_NOUNS[RAND.nextInt(GAME_NOUNS.length)] + "\"";
        }
    }
}