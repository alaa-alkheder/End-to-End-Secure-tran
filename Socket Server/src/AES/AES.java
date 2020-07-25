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

    //Encryption function
    //--------------------------------------------------------------------
    public void encryption(String key, String inputFileName)
    {
        // Generate the key
        ks.keySchedule(key);
        try
        {
            new base64().convert2hex(inputFileName);
            FileInputStream fstream = new FileInputStream("temphex");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            // Output File Name
            File f=new File(inputFileName);
            String outputFileName=  f.getName()+"Enc";;
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
                String word="";
                int j = 0;
                for(j=0;j<stringLength;j++){
                    word+=strLine.charAt(j);
                    if(word.length()==32){
                        //converting the line read into column major matrix i.e. state.
                        stringToColumnMajor(word);

                        //Initial Add RoundKey
                        addRoundKey(0);

                        for(int i=1;i<=1;i++)
                        {
                            subByte();
                            shiftRow();
                            if(i!=1)
                                mixColumn();

                            addRoundKey(i);
                        }

                        // Write Output To File
                        String enc=columnMajorToString();
                        writer.write(enc);
                        word="";
                    }
                }

                if(!word.equals("")){
                    // Padding Zeros To 32 Char
                    int k=32-(word.length()%32);
                    for(int i=0;i<k;i++)
                        word+="0";

                    stringToColumnMajor(word);

                    //Initial AddRoundKey.
                    addRoundKey(0);
                    for(int i=1;i<=1;i++)
                    {
                        subByte();
                        shiftRow();
                        if(i!=1)
                            mixColumn();

                        addRoundKey(i);
                    }

                    //W Write Output To File
                    String enc=columnMajorToString();
                    writer.write(enc);
                }
            }
            writer.close();
            br.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(Paths.get("temphex"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

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
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                System.out.print(state[i][j].toUpperCase()+" ");
            }
            System.out.print("\n");
        }

    }

    String columnMajorToString()
    {
        String str="";
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                str=str+state[j][i].toUpperCase();
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
                int b = of.rM(state[1][i], matrix[j][1]);
                int c = of.rM(state[2][i], matrix[j][2]);
                int d = of.rM(state[3][i], matrix[j][3]);
                int e = a^b^c^d;
                String s= Integer.toHexString(e);
                if (s.length()==1)
                    s="0"+s;
                temp[j]=s;
            }
            state[0][i]=temp[0];
            state[1][i]=temp[1];
            state[2][i]=temp[2];
            state[3][i]=temp[3];
        }

    }


//--------------------------------------------------------------------
    //Decryption function

 public void decryption(String key, String encFileName, String fileName, String fileType)
    {
        // Generate the key
        ks.keySchedule(key);
        try
        {
            FileInputStream fstream = new FileInputStream(encFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            //Output File Name
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
                String word="";
                int j = 0;
                for(j=0;j<stringLength;j++){
                    word+=strLine.charAt(j);
                    if(word.length()==32){
                        stringToColumnMajor(word);
                        for(int i=1;i>=1;i--)
                        {
                            addRoundKey(i);
                            if(i!=1)
                                invMixColumn();

                            invShiftRow();
                            invSubByte();

                        }
                        //Initial Add RoundKey
                        addRoundKey(0);

                        // Write Output To File
                        String dec=columnMajorToString();
                        writer.write(dec);
                        word="";
                    }
                }

                if(!word.equals("")){
                    int k=32-(word.length()%32);
                    for(int i=0;i<k;i++)
                        word+="0";

                    stringToColumnMajor(word);

                    for(int i=1;i>=1;i--)
                    {
                        addRoundKey(i);
                        if(i!=1)
                            invMixColumn();

                        invShiftRow();
                        invSubByte();
                    }
                    //Initial Add RoundKey
                    addRoundKey(0);

                    // Write Output To File
                    String dec=columnMajorToString();
                    writer.write(dec);
                }
            }
            writer.close();
            br.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        // Decoding The File After Decryption
        try {
            new base64().decode2file("tempdec",fileName,fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Delete Temp File After Decoding
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
                int b = of.rM(state[1][i], matrix[j][1]);
                int c = of.rM(state[2][i], matrix[j][2]);
                int d = of.rM(state[3][i], matrix[j][3]);
                int e = a^b^c^d;
                String s= Integer.toHexString(e);
                if (s.length()==1)
                    s="0"+s;
                temp[j]=s;
            }
            state[0][i]=temp[0];
            state[1][i]=temp[1];
            state[2][i]=temp[2];
            state[3][i]=temp[3];
        }

    }
}


