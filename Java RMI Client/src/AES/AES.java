package AES;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AES
{
    String state[][] =new String[4][4];
    OtherFunctions of=new OtherFunctions();
    SBox sb=new SBox();
    InvSBox isb=new InvSBox();
    KeySchedule ks=new KeySchedule();

    //--------------------------------------------------------------------
    public  void en(String []h)
    {
        AES aes=new AES();
//        String h[]={"d","keyfile","inputfile.txt.txt"};

        if(h.length!=3)
        {
            System.out.println("\n\nUsage :  java AES [e|d] keyFile inputFile.\n\nfor more details please read the README file  \n\nTerminating the program.......\n\n");
            System.exit(0);
        }

        String inputFileName=h[2];
        String encFileName=h[2];
        String keyfile = h[1];
        System.out.println(h[1]);
        if(h[0].equals("e"))
            aes.encryption(inputFileName,keyfile);
        else if(h[0].equals("d"))
            aes.decryption(encFileName,keyfile,"","");
        else
        {
            System.out.println("\n\nWrong parameter used. Please either Encrypt(e) or decrypt(d) the file\n\nTerminating the Program.......\n\n");
            System.exit(0);
        }
    }
    //--------------------------------------------------------------------
    public   void encryption(String key,String inputFileName)
    {
        ks.keySchedule(key); // Generate the key-full from cipher key

        try
        {

//            URL url = AES.class.getResource(inputFileName);  //for relative path
//            String path = url.getPath();
//            File file = new File(path);
//            System.out.println(url.getPath());
            new base64().convert2hex(inputFileName);
            FileInputStream fstream = new FileInputStream("temphex");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            //creating an output text file.
//            path=path.substring(0, path.length()-inputFileName.length());
//            System.out.println(path);
            String outputFileName="tempenc";
            PrintWriter writer=null;
            try
            {
                writer = new PrintWriter(outputFileName, "UTF-8");
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("Error in creating the new PrintWriter");
            }


            while ((strLine = br.readLine()) != null)
            {
                int stringLength = strLine.length();
                //System.out.println (stringLength);
                // making length equal to 32 with padding or removing extra chars
//				if(stringLength<32)
//				{
//					for(int i=0;i<(32-stringLength);i++)
//						strLine=strLine+"0";
//				}
//				 if(stringLength>=32)
//				{
//					strLine=strLine.substring(0,32);
                String word="";
                int j = 0;
                for(j=0;j<stringLength;j++){
                    word+=strLine.charAt(j);
                    if(word.length()==32){
//                        System.out.println (word);
                        //converting the line read into column major matrix i.e. state.
                        stringToColumnMajor(word);

                        //Initial AddRoundKey. Pass the round number as integer.
                        addRoundKey(0);
                        //Next 13 rounds
//                        System.out.println("After addRoundKey(0)");
//                        printStateLine();
                        for(int i=1;i<=14;i++)
                        {
                            subByte();
//                            System.out.println("After subByte");
//                            printStateLine();
                            shiftRow();
//                            System.out.println("After shiftRow");
//                            printStateLine();
                            if(i!=14)
                            {
                                mixColumn();
//                                System.out.println("After mixColumn");
//                                printStateLine();
                            }
                            addRoundKey(i);
//                            System.out.println("After addRoundKey("+i+")");
//                            printStateLine();
                        }

//                        System.out.println("The Cipher Text is : ");
//                        printState();

                        //Writing to output file
                        String enc=columnMajorToString();
                        //System.out.println("The Cipher Text is : "+enc);
                        writer.write(enc);
                        word="";
                    }
                }

                if(!word.equals("")){
                    int k=32-(word.length()%32);
                    for(int i=0;i<k;i++)
                        word+="0";
//                    System.out.println (word);
                    //converting the line read into column major matrix i.e. state.
                    stringToColumnMajor(word);

                    //Initial AddRoundKey. Pass the round number as integer.
                    addRoundKey(0);
                    //Next 13 rounds
//                    System.out.println("After addRoundKey(0)");
//                    printStateLine();
                    for(int i=1;i<=14;i++)
                    {
                        subByte();
//                        System.out.println("After subByte");
//                        printStateLine();
                        shiftRow();
//                        System.out.println("After shiftRow");
//                        printStateLine();
                        if(i!=14)
                        {
                            mixColumn();
//                            System.out.println("After mixColumn");
//                            printStateLine();
                        }
                        addRoundKey(i);
//                        System.out.println("After addRoundKey("+i+")");
//                        printStateLine();
                    }

//                    System.out.println("The Cipher Text is : ");
//                    printState();

                    //Writing to output file
                    String enc=columnMajorToString();
                    //System.out.println("The Cipher Text is : "+enc);
                    writer.write(enc);
                }

//				}



            }
            writer.close();
            br.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Please check if all filenames are correct. Files should exist in the same folder.");
        }

        try {
            Files.deleteIfExists(Paths.get("temphex"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }//Encryption function ends here.

    //--------------------------------------------------------------------
    void stringToColumnMajor(String str)
    {
        int k=0;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                state[j][i]=str.charAt(k++)+""+str.charAt(k++);
            }
        }
    }
    //--------------------------------------------------------------------
    void printState()
    {
        //System.out.println("Printing State -------");
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                System.out.print(state[i][j].toUpperCase()+" ");
            }
            System.out.print("\n");
        }

    }
//--------------------------------------------------------------------

    void printStateLine()
    {

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                System.out.print(state[j][i].toUpperCase());
            }
        }
        System.out.println();
    }



    String columnMajorToString()
    {
        String str="";
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                str=str+state[j][i].toUpperCase();
                //System.out.print(str);

            }
        }
        //System.out.println();
        return str;
    }
    //--------------------------------------------------------------------
    void addRoundKey(int round)
    {

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                state[i][j]=of.xorStringWithString(state[i][j], ks.expandedKey[i][j+(round*4)])	;
            }
        }
    }

    //--------------------------------------------------------------------
    void subByte()
    {

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                state[i][j]=sb.sBox(state[i][j]);
            }
        }
    }
    //--------------------------------------------------------------------
    void shiftRow()
    {
        String t=state[1][0];
        state[1][0]=state[1][1];
        state[1][1]=state[1][2];
        state[1][2]=state[1][3];
        state[1][3]=t;

        t=state[2][0];
        state[2][0]=state[2][2];
        state[2][2]=t;
        t=state[2][1];
        state[2][1]=state[2][3];
        state[2][3]=t;

        t=state[3][3];
        state[3][3]=state[3][2];
        state[3][2]=state[3][1];
        state[3][1]=state[3][0];
        state[3][0]=t;

    }
    //--------------------------------------------------------------------
    void mixColumn()
    {
        int matrix[][]=new int[][]
                {
                        {2,3,1,1},
                        {1,2,3,1},
                        {1,1,2,3},
                        {3,1,1,2},
                };

        for(int i=0;i<4;i++)
        {   String temp[]=new String[4];
            for(int j=0;j<4;j++)
            {
                int a = of.rM(state[0][i], matrix[j][0]);
                //System.out.println("---------------"+Integer.toHexString(a));
                int b = of.rM(state[1][i], matrix[j][1]);
                //System.out.println("---------------"+Integer.toHexString(b));
                int c = of.rM(state[2][i], matrix[j][2]);
                //System.out.println("---------------"+Integer.toHexString(c));
                int d = of.rM(state[3][i], matrix[j][3]);
                //System.out.println("---------------"+Integer.toHexString(d));
                int e = a^b^c^d;
                String s=Integer.toHexString(e);
                if (s.length()==1)
                    s="0"+s;
                //System.out.println(s);
                temp[j]=s;
            }
            state[0][i]=temp[0];
            state[1][i]=temp[1];
            state[2][i]=temp[2];
            state[3][i]=temp[3];
        }

    }

//--------------------------------------------------------------------
//--------------------------------------------------------------------
    //Decryption function starts here.

    public void decryption(String key,String encFileName,String fileName,String fileType)
    {
        ks.keySchedule(key); // Generate the key-full from cipher key

        try
        {

//            URL url = AES.class.getResource(encFileName);  //for relative path
//            String path = url.getPath();
//            File file = new File(encFileName);
            //System.out.println(url.getPath());
            FileInputStream fstream = new FileInputStream(encFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            //creating an output text file.
//            path=path.substring(0, path.length()-encFileName.length());
            //System.out.println(path);
            String outputFileName="tempdec";
            PrintWriter writer=null;
            try
            {
                writer = new PrintWriter(outputFileName, "UTF-8");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


            while ((strLine = br.readLine()) != null)
            {
                int stringLength = strLine.length();
                //System.out.println (stringLength);
                // making length equal to 32 with padding or removing extra chars
//				if(stringLength<32)
//				{
//					for(int i=0;i<(32-stringLength);i++)
//						strLine=strLine+"0";
//				}
//				 if(stringLength>=32)
//				{
//					strLine=strLine.substring(0,32);
                String word="";
                int j = 0;
                for(j=0;j<stringLength;j++){
                    word+=strLine.charAt(j);
                    if(word.length()==32){
//                        System.out.println (word);
                        //converting the line read into column major matrix i.e. state.
                        stringToColumnMajor(word);

                        //Initial AddRoundKey. Pass the round number as integer.
                        addRoundKey(0);
                        //Next 13 rounds
//                        System.out.println("After addRoundKey(0)");
//                        printStateLine();
                        for(int i=14;i>=1;i--)
                        {
                            addRoundKey(i);
//                            System.out.println("After addRoundKey("+i+")");
//                            printStateLine();

                            if(i!=14)
                            {
                                invMixColumn();
//                                System.out.println("After mixColumn");
//                                printStateLine();
                            }

                            invShiftRow();
//                            System.out.println("After shiftRow");
//                            printStateLine();

                            invSubByte();
//                            System.out.println("After subByte");
//                            printStateLine();



                        }
                        //Initial AddRoundKey. Pass the round number as integer.
                        addRoundKey(0);
//                        System.out.println("After addRoundKey(0)");
//                        printStateLine();
//                        System.out.println("The Plain Text is : ");
//                        printState();

                        //Writing to output file
                        String dec=columnMajorToString();
                        //System.out.println("The Cipher Text is : "+dec);
                        writer.write(dec);
                        word="";
                    }
                }

                if(!word.equals("")){
                    int k=32-(word.length()%32);
                    for(int i=0;i<k;i++)
                        word+="0";
//                    System.out.println (word);
                    //converting the line read into column major matrix i.e. state.
                    stringToColumnMajor(word);

                    //Initial AddRoundKey. Pass the round number as integer.
                    addRoundKey(0);
                    //Next 13 rounds
//                    System.out.println("After addRoundKey(0)");
//                    printStateLine();
                    for(int i=14;i>=1;i--)
                    {
                        addRoundKey(i);
//                        System.out.println("After addRoundKey("+i+")");
//                        printStateLine();

                        if(i!=14)
                        {
                            invMixColumn();
//                            System.out.println("After mixColumn");
//                            printStateLine();
                        }

                        invShiftRow();
//                        System.out.println("After shiftRow");
//                        printStateLine();

                        invSubByte();
//                        System.out.println("After subByte");
//                        printStateLine();



                    }
                    //Initial AddRoundKey. Pass the round number as integer.
                    addRoundKey(0);
//                    System.out.println("After addRoundKey(0)");
//                    printStateLine();
//                    System.out.println("The Plain Text is : ");
//                    printState();

                    //Writing to output file
                    String dec=columnMajorToString();
                    //System.out.println("The Cipher Text is : "+dec);
                    writer.write(dec);
                }

//				}



            }
            writer.close();
            br.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        try {
            new base64().decode2file("tempdec",fileName,fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.deleteIfExists(Paths.get("tempdec"));
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    //------------------------------------------------------------------------------------------------
    void invSubByte()
    {

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                state[i][j]=isb.sBox(state[i][j]);
            }
        }
    }
    //--------------------------------------------------------------------
    void invShiftRow()
    {
        String t=state[1][3];
        state[1][3]=state[1][2];
        state[1][2]=state[1][1];
        state[1][1]=state[1][0];
        state[1][0]=t;

        t=state[2][3];
        state[2][3]=state[2][1];
        state[2][1]=t;
        t=state[2][2];
        state[2][2]=state[2][0];
        state[2][0]=t;

        t=state[3][0];
        state[3][0]=state[3][1];
        state[3][1]=state[3][2];
        state[3][2]=state[3][3];
        state[3][3]=t;

    }
    //--------------------------------------------------------------------
    void invMixColumn()
    {
        int matrix[][]=new int[][]
                {
                        {14,11,13,9},
                        {9,14,11,13},
                        {13,9,14,11},
                        {11,13,9,14},
                };

        for(int i=0;i<4;i++)
        {   String temp[]=new String[4];
            for(int j=0;j<4;j++)
            {
                int a = of.rM(state[0][i], matrix[j][0]);
                //System.out.println("---------------"+Integer.toHexString(a));
                int b = of.rM(state[1][i], matrix[j][1]);
                //System.out.println("---------------"+Integer.toHexString(b));
                int c = of.rM(state[2][i], matrix[j][2]);
                //System.out.println("---------------"+Integer.toHexString(c));
                int d = of.rM(state[3][i], matrix[j][3]);
                //System.out.println("---------------"+Integer.toHexString(d));
                int e = a^b^c^d;
                String s=Integer.toHexString(e);
                if (s.length()==1)
                    s="0"+s;
                //System.out.println(s);
                temp[j]=s;
            }
            state[0][i]=temp[0];
            state[1][i]=temp[1];
            state[2][i]=temp[2];
            state[3][i]=temp[3];
        }

    }
}


