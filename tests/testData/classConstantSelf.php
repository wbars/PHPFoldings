<?php
define("a", 1);
class A {<fold text='...'>
    const b = 1;
    public function a() {<fold text='...'>
        <fold text=''>self::</fold>b;
        A::b;
        a;
    </fold>}
</fold>}