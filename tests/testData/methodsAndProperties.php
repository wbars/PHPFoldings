<?php
class A {<fold text='...'>
    public $b;
    public function aa() {}
</fold>}

class B {<fold text='...'>
    protected $a;
    protected static $c;
    public function aa() {<fold text='...'>
        <fold text=''>$this-></fold>a;
        <fold text=''>$this-></fold>bb();
        $b = "a";
        $this->$b;
        $this->$b();

        <fold text=''>self::</fold>bb();
        <fold text=''>self::</fold>$c;

        $a = new A();
        $a->aa();
        $a->b;

        A::bb();
        A::$c;
    </fold>}
    public function bb() {}
    public static function cc() {}
</fold>}