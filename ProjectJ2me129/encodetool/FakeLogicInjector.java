package encodetool;

import org.objectweb.asm.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * HTTH Real-Code Logic Faker & Control-Flow Weaver (Ultra-Optimized).
 * - Tự động phát hiện và chỉ can thiệp vào các method logic thực tế (>= 8 chỉ lệnh bytecode).
 * - Bỏ qua toàn bộ getter/setter/method 1 dòng để giữ JAR siêu nhẹ, tối ưu bộ nhớ KVM và giữ tốc độ 100%.
 * - Sử dụng 4 mẫu vị từ mờ đục (Opaque Predicates) cao cấp:
 *   + Mẫu 1: Bất biến tích 2 số liên tiếp (n * (n + 1)) luôn chẵn (& 1 == 0).
 *   + Mẫu 2: Bất biến bitmask lẻ (n | 1) luôn có bit cuối bằng 1 (& 1 != 0).
 *   + Mẫu 3: CRes.guard(salt) xác thực runtime.
 *   + Mẫu 4: Bất biến số bù hai (n ^ ~n == -1).
 * - Toàn bộ thao tác hoàn toàn trên Stack, không tốn thêm local variable slot.
 * - Đánh lừa hoàn toàn CFR, Procyon, Fernflower, JD-GUI.
 */
public final class FakeLogicInjector {

    private static final SecureRandom RAND = new SecureRandom();
    private static final int MIN_INSTRUCTION_COUNT = 8;

    public static void main(String[] args) throws Exception {
        String classesDir = args.length > 0 ? args[0] : "build/classes";
        Path rootPath = Paths.get(classesDir);
        if (!Files.exists(rootPath)) {
            System.err.println("Directory not found: " + classesDir);
            return;
        }

        System.out.println("[*] [HTTH Real-Code Logic Faker] Optimizing & warping substantive methods in: " + classesDir);

        int[] totalMethodsWarped = new int[]{0};
        int[] processedClasses = new int[]{0};

        try (Stream<Path> paths = Files.walk(rootPath)) {
            for (Path path : (Iterable<Path>) paths::iterator) {
                if (Files.isRegularFile(path) && path.toString().endsWith(".class") && !path.toString().contains("encodetool")) {
                    String className = rootPath.relativize(path).toString()
                            .replace(File.separatorChar, '/')
                            .replace(".class", "");

                    String simpleName = className.contains("/") ? className.substring(className.lastIndexOf('/') + 1) : className;

                    // Không can thiệp vào chính CRes và công cụ build
                    if (simpleName.equals("CRes") || simpleName.startsWith("encodetool")) {
                        continue;
                    }

                    byte[] originalBytes = Files.readAllBytes(path);
                    byte[] transformed = transformClass(originalBytes, simpleName, totalMethodsWarped);

                    if (transformed != null && transformed != originalBytes) {
                        Files.write(path, transformed);
                        processedClasses[0]++;
                    }
                }
            }
        }

        System.out.println("[+] [HTTH Real-Code Logic Faker] Successfully faked and warped " + totalMethodsWarped[0] + " substantive methods across " + processedClasses[0] + " classes (Lightweight & 100% Functioning)!");
    }

    private static byte[] transformClass(byte[] classBytes, final String className, final int[] totalCount) {
        try {
            ClassReader cr = new ClassReader(classBytes);

            // Pass 1: Đếm số chỉ lệnh bytecode trong từng method để lọc method thực sự có logic
            final Map<String, Integer> insnCounts = new HashMap<>();
            cr.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    final String key = name + descriptor;
                    return new MethodVisitor(Opcodes.ASM9) {
                        private int count = 0;

                        @Override
                        public void visitInsn(int opcode) { count++; }
                        @Override
                        public void visitIntInsn(int opcode, int operand) { count++; }
                        @Override
                        public void visitVarInsn(int opcode, int varIndex) { count++; }
                        @Override
                        public void visitTypeInsn(int opcode, String type) { count++; }
                        @Override
                        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) { count++; }
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) { count++; }
                        @Override
                        public void visitJumpInsn(int opcode, Label label) { count++; }
                        @Override
                        public void visitLdcInsn(Object value) { count++; }
                        @Override
                        public void visitIincInsn(int varIndex, int increment) { count++; }
                        @Override
                        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) { count++; }
                        @Override
                        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) { count++; }
                        @Override
                        public void visitMultiANewArrayInsn(String descriptor, int numDimensions) { count++; }

                        @Override
                        public void visitEnd() {
                            insnCounts.put(key, count);
                        }
                    };
                }
            }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            // Pass 2: Tiêm fake logic vào những method đạt tiêu chuẩn
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            final int[] classWarped = new int[]{0};

            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                private boolean isInterface = false;

                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    if ((access & Opcodes.ACC_INTERFACE) != 0) {
                        isInterface = true;
                    }
                    super.visit(version, access, name, signature, superName, interfaces);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    if (isInterface || mv == null) return mv;

                    // Tuyệt đối không can thiệp vào constructor (<init>) hoặc static initializer (<clinit>)
                    if (name.equals("<init>") || name.equals("<clinit>")) return mv;
                    // Bỏ qua abstract, native, synthetic, bridge
                    if ((access & (Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_BRIDGE)) != 0) return mv;

                    // Chỉ tiêm vào method có kích thước đủ lớn (tránh làm nặng getters/setters/empty hooks)
                    Integer count = insnCounts.get(name + descriptor);
                    if (count == null || count < MIN_INSTRUCTION_COUNT) {
                        return mv;
                    }
                    if (name.startsWith("get") && count < 12) {
                        return mv;
                    }
                    if (name.startsWith("set") && count < 10) {
                        return mv;
                    }

                    final Type retType = Type.getReturnType(descriptor);

                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        private boolean injected = false;

                        @Override
                        public void visitCode() {
                            super.visitCode();
                            if (!injected) {
                                injected = true;
                                int salt = RAND.nextInt(60000) + 1000;
                                int pattern = RAND.nextInt(4);
                                Label realCode = new Label();

                                // Tiêm Opaque Predicate hoàn toàn trên stack
                                if (pattern == 0) {
                                    // Invariant: ((salt * (salt + 1)) & 1) == 0 (Tích 2 số liên tiếp luôn là số chẵn)
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitInsn(Opcodes.DUP);
                                    super.visitInsn(Opcodes.ICONST_1);
                                    super.visitInsn(Opcodes.IADD);
                                    super.visitInsn(Opcodes.IMUL);
                                    super.visitInsn(Opcodes.ICONST_1);
                                    super.visitInsn(Opcodes.IAND);
                                    super.visitJumpInsn(Opcodes.IFEQ, realCode);
                                } else if (pattern == 1) {
                                    // Invariant: ((salt | 1) & 1) != 0 (OR với 1 luôn ra số lẻ)
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitInsn(Opcodes.ICONST_1);
                                    super.visitInsn(Opcodes.IOR);
                                    super.visitInsn(Opcodes.ICONST_1);
                                    super.visitInsn(Opcodes.IAND);
                                    super.visitJumpInsn(Opcodes.IFNE, realCode);
                                } else if (pattern == 2) {
                                    // Invariant: CRes.guard(salt) runtime guard
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "CRes", "guard", "(I)Z", false);
                                    super.visitJumpInsn(Opcodes.IFNE, realCode);
                                } else {
                                    // Invariant: ((salt ^ ~salt) == -1)
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitInsn(Opcodes.ICONST_M1);
                                    super.visitInsn(Opcodes.IXOR);
                                    super.visitInsn(Opcodes.IXOR);
                                    super.visitInsn(Opcodes.ICONST_M1);
                                    super.visitJumpInsn(Opcodes.IF_ICMPEQ, realCode);
                                }

                                // --- FAKE BRANCH (Đánh lừa decompiler, không bao giờ chạy ở runtime) ---
                                int decoyCall = RAND.nextInt(2);
                                if (decoyCall == 0) {
                                    super.visitLdcInsn(Integer.valueOf(salt ^ 0xAA55));
                                    super.visitLdcInsn(Integer.valueOf(42));
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "CRes", "fakeAction", "(II)I", false);
                                    super.visitInsn(Opcodes.POP);
                                } else {
                                    super.visitLdcInsn(Integer.valueOf(salt));
                                    super.visitLdcInsn(Integer.valueOf(salt * 31));
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "CRes", "fakeValidate", "(II)I", false);
                                    super.visitInsn(Opcodes.POP);
                                }

                                // Trả về giá trị giả lập phù hợp kiểu trả về của method
                                switch (retType.getSort()) {
                                    case Type.VOID:
                                        super.visitInsn(Opcodes.RETURN);
                                        break;
                                    case Type.BOOLEAN:
                                    case Type.BYTE:
                                    case Type.CHAR:
                                    case Type.SHORT:
                                    case Type.INT:
                                        super.visitInsn(Opcodes.ICONST_0);
                                        super.visitInsn(Opcodes.IRETURN);
                                        break;
                                    case Type.LONG:
                                        super.visitInsn(Opcodes.LCONST_0);
                                        super.visitInsn(Opcodes.LRETURN);
                                        break;
                                    case Type.FLOAT:
                                        super.visitInsn(Opcodes.FCONST_0);
                                        super.visitInsn(Opcodes.FRETURN);
                                        break;
                                    case Type.DOUBLE:
                                        super.visitInsn(Opcodes.DCONST_0);
                                        super.visitInsn(Opcodes.DRETURN);
                                        break;
                                    default: // Object hoặc Array
                                        super.visitInsn(Opcodes.ACONST_NULL);
                                        super.visitInsn(Opcodes.ARETURN);
                                        break;
                                }

                                // --- REAL CODE CHÍNH BẮT ĐẦU TẠI ĐÂY ---
                                super.visitLabel(realCode);
                                classWarped[0]++;
                                totalCount[0]++;
                            }
                        }
                    };
                }
            };

            cr.accept(cv, 0);

            if (classWarped[0] > 0) {
                return cw.toByteArray();
            }
        } catch (Exception e) {
            // Giữ nguyên bytecode nếu class không thể parse
        }
        return classBytes;
    }
}
