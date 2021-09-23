# River
使用方式与JDK Stream类似
## 创建一个River
可以直接在of中输入元素
```java
River<String> river = River.of("1", "1", "2", "3", "3");
```
或者传入一个容器对象
```java
List<String> strList = Arrays.asList("1", "2", "3");
River<String> river = River.of(strList);
```

## 基本中间操作
River支持的中间操作有
- filter()
- distinct()
- map()
- limit()
- sort()
- peek()
- skip()

### 使用示例
filter()
```java
River.of("1", "1", "2", "3", "3").filter(e -> e == "2");
```
distince()
```java
River.of("1", "1", "2", "3", "3").distinct();
```
## 基本终结操作
- toArray()
- toArray(E[])
- reduce(identity,binaryOperator)
- collect()
- min()
- max()
- anyMatch()
- allMatch()
- noneMatch()
- findFirst()

### 使用示例
```java
public static void minTest() {
    Optional<Integer> min = River.of(1, 2, 2, 4, 0, 5, -2)
            .min((a, b) -> {
                if (a >= b) {
                    return 1;
                } else {
                    return -1;
                }
            });
    assert min.isPresent();
    Integer minVal = min.get();
    assert minVal == -2;
}
```
```java
public static void allMatch() {
    boolean b1 = River.of(1, 2, 2, 4, 0, 5)
            .allMatch(e -> e <= 5);
    assert b1;
    
    boolean b2 = River.of(1, 2, 2, 4, 0, 5)
            .anyMatch(e -> e > 5);
    assert !b2;
}
```

## 串行流与并行流
- parallel() : 将流转换为并行流
- sequential() : 将流转换为串行流
设置并行流后，其他操作与串行流一致。

    boolean b2 = River.of(1, 2, 2, 4, 0, 5)xin
    boolean b2 = River.of(1, 2, 2, 4, 0, 5)
    
![image](https://user-images.githubusercontent.com/34532365/134490464-c6ce0459-eaa5-4cbf-9922-a46e99e7ebab.png)
