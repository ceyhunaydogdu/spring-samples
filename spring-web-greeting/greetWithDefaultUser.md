# GreetingControllerTest#greetWithDefaultUser()

```java

@Test
public void greetWithDefaultUser() throws Exception {
    mvc
        .perform(get("/greeting"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Hi, Spring Booter!, how are you today?")));
}

```
