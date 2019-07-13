# GreetingControllerTest#greetWithCustomUser()

```java

@Test
public void greetWithCustomUser() throws Exception {
    mvc
        .perform(get("/greeting").param("name", "Ceyhun"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Hi, Ceyhun!, how are you today?")));
}

```
