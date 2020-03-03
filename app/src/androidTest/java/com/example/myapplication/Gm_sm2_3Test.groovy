package com.example.myapplication

import org.junit.Before
import org.junit.Test

class Gm_sm2_3Test extends GroovyTestCase {
    @Before
    void setUp() {
        super.setUp()
        Gm_sm2_3 gm=new Gm_sm2_3()
    }
    @Test
    void testGM_GenSM2keypair() {
        byte []pub=new byte[64]
        Gm_sm2_3.GM_GenSM2keypair(pub)
    }
}
