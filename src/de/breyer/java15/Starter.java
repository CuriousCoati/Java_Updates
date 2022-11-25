package de.breyer.java15;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.textBlocks();
        starter.detailedNPE();

        // ZGC and Shenandoah GCs are no longer experimental, but G1 ist still the default

        // DatagramSocket API was rewritten wit NIO, like Socket API in java 13
    }

    private void detailedNPE() {
        System.out.println("### detailed NPE ###");

        try {
            // with java 14 NPEs are more detailed
            String[] messages = null;
            messages[0] = "NPE";
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void textBlocks() {
        System.out.println("### text blocks ###");

        // text blocks were introduced to simplify multiline literals
        // base indentation and first new line are  not  part of the resulting string
        // \ can be used to have a visible line break, which should not be present in the resulting string
        // \s can be used to escape a whitespace, otherwise trailing whitespaces will be removed

        String largeText = """
                <html>     \s
                    <head>
                        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" \
                integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
                \
                    </head>
                    <body>
                    </body>
                </html>\
                """;
        System.out.println(largeText);

        System.out.println();
    }

}
