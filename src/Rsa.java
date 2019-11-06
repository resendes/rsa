import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Properties;

public class Rsa {

    public static final String FILE_NAME = "message.txt";

    public static void encrypt(BigInteger n, BigInteger e) throws IOException {
        FileInputStream publicKeyFile = new FileInputStream("rsa_public.txt");
        Properties publicKey = new Properties();
        publicKey.load(publicKeyFile);

        File fileName = new File(FILE_NAME);
        DataInputStream message = new DataInputStream(new FileInputStream(FILE_NAME));
        DataOutputStream encryptedMessage = new DataOutputStream(new FileOutputStream(FILE_NAME + ".enc"));

        int nLength = n.bitLength() / 8;
        encryptedMessage.writeInt((int) (fileName.length() % nLength));
        byte[] inArr = new byte[nLength + 1];
        byte[] tempArr, outArr = new byte[nLength + 1];
        int count;
        BigInteger P, C;

        while ((count = message.read(inArr, 1, nLength)) != -1) {
            inArr[0] = 0;
            if (count < nLength) {
                System.arraycopy(inArr, 1, inArr, nLength + 1 - count, count);
                Arrays.fill(inArr, 1, nLength + 1 - count, (byte) 0);
            }
            P = new BigInteger(inArr);
            C = P.modPow(e, n);
            tempArr = C.toByteArray();
            System.arraycopy(tempArr, 0, outArr, nLength - tempArr.length + 1, tempArr.length);
            Arrays.fill(outArr, 0, nLength - tempArr.length + 1, (byte) 0);
            encryptedMessage.write(outArr);

        }

        message.close();
        encryptedMessage.close();

    }

    public static void decrypt(BigInteger n, BigInteger d) throws IOException {
        FileInputStream privateKeyFile = new FileInputStream("rsa_private.txt");
        Properties privateKey = new Properties();
        privateKey.load(privateKeyFile);

        DataInputStream encryptedMessage = new DataInputStream(new FileInputStream(FILE_NAME + ".enc"));
        DataOutputStream decryptedMessage = new DataOutputStream(new FileOutputStream(FILE_NAME + ".dec"));

        int fileLength = encryptedMessage.readInt();

        int nLength = n.bitLength() / 8;
        byte[] inActualArr = new byte[nLength + 1], inProxArr = new byte[nLength + 1];
        byte[] tempArr, outArr = new byte[nLength];
        BigInteger P, C;
        int count, outLength = nLength;

        count = encryptedMessage.read(inProxArr);

        while (count != -1) {
            System.arraycopy(inProxArr, 0, inActualArr, 0, inProxArr.length);
            C = new BigInteger(inActualArr);
            P = C.modPow(d, n);
            tempArr = P.toByteArray();
            count = encryptedMessage.read(inProxArr);

            if (count == -1) {
                outLength = (fileLength == 0) ? nLength : fileLength;
            }

            int startIndex = (tempArr.length == (nLength + 1)) ? 1 : 0;

            System.arraycopy(tempArr, startIndex, outArr, outArr.length - (tempArr.length - startIndex), tempArr.length - startIndex);
            Arrays.fill(outArr, 0, outArr.length - (tempArr.length - startIndex), (byte) 0);
            decryptedMessage.write(outArr, nLength - outLength, outLength);
        }

        encryptedMessage.close();
        decryptedMessage.close();
    }
}
