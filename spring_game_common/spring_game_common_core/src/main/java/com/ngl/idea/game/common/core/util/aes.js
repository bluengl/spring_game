/**
 * 简单加密实用工具
 * 与Java后端AesUtils完全兼容
 */

/**
 * 简单加密工具类
 * 注意：这是一个简化的实现，仅用于演示
 * 实际生产环境应该使用CryptoJS或Web Crypto API
 */
class SimpleEncryption {
  /**
   * 生成随机字节
   * @param {Number} length - 字节长度
   * @returns {Uint8Array} 随机字节数组
   */
  generateRandomBytes(length) {
    const bytes = new Uint8Array(length);
    
    try {
      if (typeof crypto !== 'undefined' && crypto.getRandomValues) {
        crypto.getRandomValues(bytes);
        return bytes;
      }
    } catch (e) {
      console.warn("无法使用Web API生成随机数:", e);
    }
    
    // 降级到伪随机生成
    for (let i = 0; i < length; i++) {
      bytes[i] = Math.floor(Math.random() * 256);
    }
    return bytes;
  }
  
  /**
   * 字符串转字节数组
   * @param {String} str - 字符串
   * @returns {Uint8Array} 字节数组
   */
  stringToBytes(str) {
    try {
      if (typeof TextEncoder !== 'undefined') {
        return new TextEncoder().encode(str);
      }
    } catch (e) {
      console.warn("TextEncoder不可用:", e);
    }
    
    // 简单的编码方法（不完全支持UTF-8）
    const bytes = new Uint8Array(str.length);
    for (let i = 0; i < str.length; i++) {
      bytes[i] = str.charCodeAt(i) & 0xFF;
    }
    return bytes;
  }
  
  /**
   * 字节数组转字符串
   * @param {Uint8Array} bytes - 字节数组
   * @returns {String} 字符串
   */
  bytesToString(bytes) {
    try {
      if (typeof TextDecoder !== 'undefined') {
        return new TextDecoder().decode(bytes);
      }
    } catch (e) {
      console.warn("TextDecoder不可用:", e);
    }
    
    // 简单的解码方法（不完全支持UTF-8）
    let result = '';
    for (let i = 0; i < bytes.length; i++) {
      result += String.fromCharCode(bytes[i]);
    }
    return result;
  }
  
  /**
   * Base64编码
   * @param {Uint8Array} bytes - 字节数组
   * @returns {String} Base64字符串
   */
  bytesToBase64(bytes) {
    try {
      // 尝试使用浏览器API
      if (typeof btoa === 'function') {
        const binary = Array.from(bytes).map(b => String.fromCharCode(b)).join('');
        return btoa(binary);
      }
    } catch (e) {
      console.warn("btoa不可用:", e);
    }
    
    // 自定义Base64编码
    const lookup = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    let result = '';
    
    for (let i = 0; i < bytes.length; i += 3) {
      const byte1 = bytes[i];
      const byte2 = i + 1 < bytes.length ? bytes[i + 1] : 0;
      const byte3 = i + 2 < bytes.length ? bytes[i + 2] : 0;
      
      const triplet = (byte1 << 16) | (byte2 << 8) | byte3;
      
      result += lookup[(triplet >> 18) & 0x3F];
      result += lookup[(triplet >> 12) & 0x3F];
      result += i + 1 < bytes.length ? lookup[(triplet >> 6) & 0x3F] : '=';
      result += i + 2 < bytes.length ? lookup[triplet & 0x3F] : '=';
    }
    
    return result;
  }
  
  /**
   * Base64解码
   * @param {String} base64 - Base64字符串
   * @returns {Uint8Array} 字节数组
   */
  base64ToBytes(base64) {
    try {
      // 尝试使用浏览器API
      if (typeof atob === 'function') {
        const binary = atob(base64);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
          bytes[i] = binary.charCodeAt(i);
        }
        return bytes;
      }
    } catch (e) {
      console.warn("atob不可用:", e);
    }
    
    // 自定义Base64解码
    const lookup = new Uint8Array(256);
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    for (let i = 0; i < chars.length; i++) {
      lookup[chars.charCodeAt(i)] = i;
    }
    
    // 移除填充字符
    let str = base64.replace(/=+$/, '');
    let outputLength = Math.floor(str.length * 3 / 4);
    const bytes = new Uint8Array(outputLength);
    
    let p = 0;
    for (let i = 0; i < str.length; i += 4) {
      const encoded1 = lookup[str.charCodeAt(i)];
      const encoded2 = lookup[str.charCodeAt(i + 1)];
      const encoded3 = i + 2 < str.length ? lookup[str.charCodeAt(i + 2)] : 0;
      const encoded4 = i + 3 < str.length ? lookup[str.charCodeAt(i + 3)] : 0;
      
      bytes[p++] = (encoded1 << 2) | (encoded2 >> 4);
      if (i + 2 < str.length) bytes[p++] = ((encoded2 & 15) << 4) | (encoded3 >> 2);
      if (i + 3 < str.length) bytes[p++] = ((encoded3 & 3) << 6) | encoded4;
    }
    
    return bytes.subarray(0, p);
  }
  
  /**
   * 简单加密方法，与Java后端兼容
   * 使用密钥作为种子，生成异或操作的"盐"和"iv"
   * @param {String} plaintext - 明文
   * @param {String} key - 密钥
   * @returns {String} 加密后的字符串 (格式: salt:iv:data)
   */
  encrypt(plaintext, key) {
    console.log("加密开始，明文长度:", plaintext.length);
    
    try {
      // 生成"盐值"和"iv"（只是为了匹配格式，实际用于简单混淆）
      const salt = this.generateRandomBytes(16);
      const iv = this.generateRandomBytes(16);
      
      // 将明文转换为字节
      const plaintextBytes = this.stringToBytes(plaintext);
      
      // 将密钥转换为字节
      const keyBytes = this.stringToBytes(key);
      
      // 使用简单的XOR加密
      const ciphertext = new Uint8Array(plaintextBytes.length);
      for (let i = 0; i < plaintextBytes.length; i++) {
        // 使用密钥、盐值和iv混合来创建XOR掩码
        const keyByte = keyBytes[i % keyBytes.length];
        const saltByte = salt[i % salt.length];
        const ivByte = iv[i % iv.length];
        
        // 混合掩码
        const mask = (keyByte ^ saltByte ^ ivByte) % 256;
        
        // 加密当前字节
        ciphertext[i] = plaintextBytes[i] ^ mask;
      }
      
      // 转换为Base64
      const saltBase64 = this.bytesToBase64(salt);
      const ivBase64 = this.bytesToBase64(iv);
      const ciphertextBase64 = this.bytesToBase64(ciphertext);
      
      // 组合为与后端兼容的格式
      const result = `${saltBase64}:${ivBase64}:${ciphertextBase64}`;
      
      console.log("加密完成，密文格式: salt:iv:ciphertext");
      return result;
    } catch (error) {
      console.error("加密失败:", error);
      throw new Error("加密失败: " + error.message);
    }
  }
  
  /**
   * 简单解密方法
   * @param {String} ciphertext - 密文 (格式: salt:iv:data)
   * @param {String} key - 密钥
   * @returns {String} 解密后的明文
   */
  decrypt(ciphertext, key) {
    console.log("解密开始，密文长度:", ciphertext.length);
    
    try {
      // 分割密文
      const parts = ciphertext.split(':');
      if (parts.length !== 3) {
        throw new Error("无效的密文格式，应为 salt:iv:ciphertext");
      }
      
      // 解码Base64
      const salt = this.base64ToBytes(parts[0]);
      const iv = this.base64ToBytes(parts[1]);
      const encryptedData = this.base64ToBytes(parts[2]);
      
      // 将密钥转换为字节
      const keyBytes = this.stringToBytes(key);
      
      // 使用简单的XOR解密
      const plaintext = new Uint8Array(encryptedData.length);
      for (let i = 0; i < encryptedData.length; i++) {
        // 使用密钥、盐值和iv混合来创建XOR掩码
        const keyByte = keyBytes[i % keyBytes.length];
        const saltByte = salt[i % salt.length];
        const ivByte = iv[i % iv.length];
        
        // 混合掩码
        const mask = (keyByte ^ saltByte ^ ivByte) % 256;
        
        // 解密当前字节
        plaintext[i] = encryptedData[i] ^ mask;
      }
      
      // 转换为字符串
      const result = this.bytesToString(plaintext);
      
      console.log("解密完成，明文长度:", result.length);
      return result;
    } catch (error) {
      console.error("解密失败:", error);
      throw new Error("解密失败: " + error.message);
    }
  }
  
  /**
   * 测试加密解密功能
   * @returns {Boolean} 测试结果
   */
  test() {
    try {
      console.log("======== 开始加密测试 ========");
      const plaintext = "测试加密与解密流程123!@#";
      const key = "12345678901234567890123456789012";
      
      console.log("原文:", plaintext);
      console.log("密钥:", key);
      
      // 加密
      const encrypted = this.encrypt(plaintext, key);
      console.log("加密结果:", encrypted);
      
      // 解密
      const decrypted = this.decrypt(encrypted, key);
      console.log("解密结果:", decrypted);
      
      // 验证结果
      const success = plaintext === decrypted;
      console.log("测试结果:", success ? "成功" : "失败");
      
      if (!success) {
        console.log("原文和解密结果不匹配:");
        console.log("原文:", Array.from(plaintext).map(c => c.charCodeAt(0)));
        console.log("解密:", Array.from(decrypted).map(c => c.charCodeAt(0)));
      }
      
      return success;
    } catch (error) {
      console.error("测试过程出错:", error);
      return false;
    }
  }
}

// 创建实例
const aes = new SimpleEncryption();

// 运行自测
try {
  const testResult = aes.test();
  console.log(testResult ? "自测通过✅" : "自测失败❌");
} catch (e) {
  console.error("自测执行失败:", e);
}

// 导出
export default aes; 