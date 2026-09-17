using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using Mono.Beebyte.Cecil;
using Mono.Beebyte.Cecil.Cil;
using UnityEditor;
using UnityEngine;

namespace HTTHSecurity
{
    public static class HTTHLogicHider
    {
        private static readonly byte[] AES_KEY = new byte[] { 0x48, 0x54, 0x54, 0x48, 0x5F, 0x56, 0x49, 0x50, 0x32, 0x30, 0x32, 0x36, 0x40, 0x21, 0x23, 0x24 };

        public static void ProcessAssembly(string assemblyPath, string outputPath, bool hideAllLogic, bool injectFakeLogic, bool encryptStrings)
        {
            if (!File.Exists(assemblyPath))
            {
                Debug.LogError("[HTTH Security] Assembly not found: " + assemblyPath);
                return;
            }

            Debug.Log("[HTTH Security] Starting Ultimate Protection on: " + assemblyPath);

            var readerParams = new ReaderParameters { ReadWrite = true, InMemory = true };
            AssemblyDefinition assembly = AssemblyDefinition.ReadAssembly(assemblyPath, readerParams);
            ModuleDefinition module = assembly.MainModule;

            int strippedMethods = 0;
            int fakeMethodsAdded = 0;
            int stringsEncrypted = 0;

            // 1. Inject Fake Logic & Opaque Predicates
            if (injectFakeLogic)
            {
                fakeMethodsAdded = InjectFakeRoutines(module);
            }

            // 2. Encrypt Strings to Byte/Hex XOR
            if (encryptStrings)
            {
                stringsEncrypted = EncryptAllStrings(module);
            }

            // 3. Method Body Stripping / Logic Hiding (Only declarations & empty stubs remain)
            if (hideAllLogic)
            {
                strippedMethods = StripMethodBodies(module);
            }

            // Write output assembly
            var writerParams = new WriterParameters { WriteSymbols = false };
            assembly.Write(outputPath, writerParams);

            Debug.Log($"[HTTH Security] Protection Complete!\n" +
                      $"- Stripped & Hidden Logic: {strippedMethods} methods\n" +
                      $"- Fake Logic Injected: {fakeMethodsAdded} methods\n" +
                      $"- Strings Encrypted: {stringsEncrypted} literals\n" +
                      $"- Output Saved To: {outputPath}");
        }

        private static int StripMethodBodies(ModuleDefinition module)
        {
            int count = 0;
            foreach (var type in module.Types)
            {
                if (type.Name.Contains("HuaThanh") || type.Name.StartsWith("<")) continue;

                foreach (var method in type.Methods)
                {
                    if (!method.HasBody || method.IsConstructor || method.IsAbstract) continue;

                    method.Body.Instructions.Clear();
                    method.Body.Variables.Clear();
                    method.Body.ExceptionHandlers.Clear();

                    var il = method.Body.GetILProcessor();

                    if (method.ReturnType.FullName == "System.Void")
                    {
                        il.Emit(OpCodes.Ret);
                    }
                    else if (method.ReturnType.IsValueType)
                    {
                        if (method.ReturnType.FullName == "System.Boolean" ||
                            method.ReturnType.FullName == "System.Int32" ||
                            method.ReturnType.FullName == "System.Byte" ||
                            method.ReturnType.FullName == "System.Int16")
                        {
                            il.Emit(OpCodes.Ldc_I4_0);
                            il.Emit(OpCodes.Ret);
                        }
                        else if (method.ReturnType.FullName == "System.Int64")
                        {
                            il.Emit(OpCodes.Ldc_I8, (long)0);
                            il.Emit(OpCodes.Ret);
                        }
                        else if (method.ReturnType.FullName == "System.Single")
                        {
                            il.Emit(OpCodes.Ldc_R4, 0.0f);
                            il.Emit(OpCodes.Ret);
                        }
                        else if (method.ReturnType.FullName == "System.Double")
                        {
                            il.Emit(OpCodes.Ldc_R8, 0.0);
                            il.Emit(OpCodes.Ret);
                        }
                        else
                        {
                            il.Emit(OpCodes.Ldnull);
                            il.Emit(OpCodes.Throw);
                        }
                    }
                    else
                    {
                        il.Emit(OpCodes.Ldnull);
                        il.Emit(OpCodes.Ret);
                    }

                    count++;
                }
            }
            return count;
        }

        private static int EncryptAllStrings(ModuleDefinition module)
        {
            int count = 0;
            MethodReference decryptMethod = null;

            // Tìm method HuaThanhUnity.DecryptByteString
            foreach (var type in module.Types)
            {
                if (type.Name == "HuaThanhUnity")
                {
                    foreach (var m in type.Methods)
                    {
                        if (m.Name == "DecryptByteString")
                        {
                            decryptMethod = module.ImportReference(m);
                            break;
                        }
                    }
                }
            }

            if (decryptMethod == null) return 0;

            System.Random rand = new System.Random();

            foreach (var type in module.Types)
            {
                if (type.Name.Contains("HuaThanh")) continue;

                foreach (var method in type.Methods)
                {
                    if (!method.HasBody) continue;

                    var instructions = method.Body.Instructions;
                    for (int i = 0; i < instructions.Count; i++)
                    {
                        if (instructions[i].OpCode == OpCodes.Ldstr && instructions[i].Operand is string strVal && !string.IsNullOrEmpty(strVal))
                        {
                            int salt = rand.Next(1000, 99999);
                            byte[] raw = Encoding.UTF8.GetBytes(strVal);
                            byte[] encrypted = new byte[raw.Length];
                            for (int j = 0; j < raw.Length; j++)
                            {
                                int key = (salt ^ (j * 31 + 0x5A)) & 0xFF;
                                encrypted[j] = (byte)(raw[j] ^ key);
                            }

                            var il = method.Body.GetILProcessor();

                            // Inject byte array construction
                            var loadArrayInst = il.Create(OpCodes.Ldc_I4, encrypted.Length);
                            var newArrayInst = il.Create(OpCodes.Newarr, module.TypeSystem.Byte);

                            instructions[i] = loadArrayInst;
                            il.InsertAfter(loadArrayInst, newArrayInst);

                            Instruction last = newArrayInst;
                            for (int k = 0; k < encrypted.Length; k++)
                            {
                                var dup = il.Create(OpCodes.Dup);
                                var idx = il.Create(OpCodes.Ldc_I4, k);
                                var val = il.Create(OpCodes.Ldc_I4, (int)encrypted[k]);
                                var stelem = il.Create(OpCodes.Stelem_I1);

                                il.InsertAfter(last, dup);
                                il.InsertAfter(dup, idx);
                                il.InsertAfter(idx, val);
                                il.InsertAfter(val, stelem);
                                last = stelem;
                            }

                            var loadSalt = il.Create(OpCodes.Ldc_I4, salt);
                            var callDecrypt = il.Create(OpCodes.Call, decryptMethod);

                            il.InsertAfter(last, loadSalt);
                            il.InsertAfter(loadSalt, callDecrypt);

                            count++;
                            break; // 1 per scan pass to ensure IL offsets stay valid
                        }
                    }
                }
            }
            return count;
        }

        private static int InjectFakeRoutines(ModuleDefinition module)
        {
            int count = 0;
            string[] fakePrefixes = { "CheckNetworkPacket", "VerifyGameIntegrity", "CalculateDynamicStats", "ProcessAntiCheatSignal", "SyncCombatState" };
            System.Random rand = new System.Random();

            foreach (var type in module.Types)
            {
                if (type.IsInterface || type.IsEnum || type.Name.Contains("<")) continue;

                int injectAmount = rand.Next(2, 6);
                for (int i = 0; i < injectAmount; i++)
                {
                    string methodName = fakePrefixes[rand.Next(fakePrefixes.Length)] + "_" + rand.Next(100, 9999);
                    var method = new MethodDefinition(methodName, MethodAttributes.Public | MethodAttributes.HideBySig, module.TypeSystem.Int32);

                    var il = method.Body.GetILProcessor();
                    il.Emit(OpCodes.Ldc_I4, rand.Next(100, 5000));
                    il.Emit(OpCodes.Ldc_I4, rand.Next(10, 500));
                    il.Emit(OpCodes.Xor);
                    il.Emit(OpCodes.Ret);

                    type.Methods.Add(method);
                    count++;
                }
            }
            return count;
        }
    }
}
