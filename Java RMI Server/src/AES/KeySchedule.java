package AES;


public class KeySchedule {

    SBox sb=new SBox();
    OtherFunctions of=new OtherFunctions();
    String expandedKey[][]=new String[4][60];
    String temp[][]=new String[4][1];
    int Nk;
    public void keySchedule(String key)
    {
        try
        {
            // converting key into a matrix
            readKeyIntoMatrix(key);

            //expanding the key
            expandTheKey();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    void readKeyIntoMatrix(String str)
    {
        System.out.println(str.length());
        switch (str.length()) {
            // 128 bit key
            case 64:
            {
                Nk = 8;
                break;
            }
            // 192 bit key
            case 48:
                Nk = 6;
                break;
            // 256 bit key
            case 32:
                Nk = 4;
                break;
            default:
                throw new IllegalArgumentException("It only supports 128, 192 and 256 bit keys!");
        }


        int k=0;
        for(int i=0;i<Nk;i++)
        {
            for(int j=0;j<4;j++)
            {
                expandedKey[j][i]=str.charAt(k++)+""+str.charAt(k++);
            }
        }
    }

    void expandTheKey()
    {
        try
        {
            for(int i=Nk;i<60;i++)
            {
                //Assigning the W[i-1] to temp
                temp[0][0]=expandedKey[0][i-1];
                temp[1][0]=expandedKey[1][i-1];
                temp[2][0]=expandedKey[2][i-1];
                temp[3][0]=expandedKey[3][i-1];

                if(i%Nk==0)
                {
                    rotWord();
                    subWord();
                    xorWithRcon((i/Nk)-1);
                }

                if(i%Nk==4)
                    subWord();

                xorWithW(i-Nk);

                expandedKey[0][i]=temp[0][0];
                expandedKey[1][i]=temp[1][0];
                expandedKey[2][i]=temp[2][0];
                expandedKey[3][i]=temp[3][0];
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    void rotWord()
    {
        String t=temp[3][0];
        temp[3][0]=temp[0][0];
        temp[0][0]=temp[1][0];
        temp[1][0]=temp[2][0];
        temp[2][0]=t;
    }

    void subWord()
    {
        temp[0][0]=sb.sBox(temp[0][0]);
        temp[1][0]=sb.sBox(temp[1][0]);
        temp[2][0]=sb.sBox(temp[2][0]);
        temp[3][0]=sb.sBox(temp[3][0]);
    }

    void xorWithRcon(int i)
    {
        String rCon[][] = new String[][]
                {
                        {"01","02","03","04","10","20","40","80","1B","36","6c","d8","ab","4d","9a"},
                        {"01","02","03","04","10","20","40","80","1B","36","6c","d8","ab","4d","9a"},
                        {"01","02","03","04","10","20","40","80","1B","36","6c","d8","ab","4d","9a"},
                        {"01","02","03","04","10","20","40","80","1B","36","6c","d8","ab","4d","9a"},


                };

        temp[0][0]=of.xorStringWithString(temp[0][0], rCon[0][i]);
        temp[1][0]=of.xorStringWithString(temp[1][0], rCon[1][i]);
        temp[2][0]=of.xorStringWithString(temp[2][0], rCon[2][i]);
        temp[3][0]=of.xorStringWithString(temp[3][0], rCon[3][i]);
    }

    void xorWithW(int i)
    {
        temp[0][0]=of.xorStringWithString(temp[0][0], expandedKey[0][i]);
        temp[1][0]=of.xorStringWithString(temp[1][0], expandedKey[1][i]);
        temp[2][0]=of.xorStringWithString(temp[2][0], expandedKey[2][i]);
        temp[3][0]=of.xorStringWithString(temp[3][0], expandedKey[3][i]);
    }

    void printKeyFull()
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<60;j++)
            {
                if(j%4==0)
                    System.out.print("  ");
                System.out.print(expandedKey[i][j]+" ");
            }
            System.out.println();
        }
    }

    void printTemp(String str)
    {
        System.out.println(str+temp[0][0]+" "+temp[1][0]+" "+temp[2][0]+" "+temp[3][0]);
    }

}
