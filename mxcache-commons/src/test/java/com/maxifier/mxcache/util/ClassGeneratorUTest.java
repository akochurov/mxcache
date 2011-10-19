package com.maxifier.mxcache.util;

import com.maxifier.mxcache.asm.Type;
import org.testng.annotations.Test;

import static com.maxifier.mxcache.asm.Opcodes.ACC_PUBLIC;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 11.03.11
 * Time: 8:40
 */
@Test
public class ClassGeneratorUTest {
    private static final Type TEST_CLASS_IMPL_TYPE = Type.getType(TestClassImpl.class);

    @Test
    public static void testGenerateEmptyConstructor() throws Exception {
        ClassGenerator w = new ClassGenerator(ACC_PUBLIC, TEST_CLASS_IMPL_TYPE.getInternalName() + "2", TEST_CLASS_IMPL_TYPE);
        w.defineDefaultConstructor();
        // ���� ����������� ������ �����������, ��� ������ ������
        TestClass instance = (TestClass) w.toClass(new ClassLoader() {}).newInstance();
        assert instance.test().equals("testString");
    }
}
