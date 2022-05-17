package com.example.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.model.BoardForm;

@Controller
public class GameController {
	public static int fHp = 20;
	public static int fEp = 0;
	public static int fMax = 1;
	public static int fPp = 1;
	public static int fHand = 4;
	public static int fSc = 0;
	public static int fNc = 0;
	public static int fComb = 0;

	public static int sHp = 20;
	public static int sEp = 3;
	public static int sMax = 1;
	public static int sPp = 1;
	public static int sHand = 4;
	public static int sSc = 0;
	public static int sNc = 0;
	public static int sComb = 0;
	
	public static int turn = 1;
	
	@Autowired
    private SimpMessagingTemplate template;

	@GetMapping("/board")
	private String init(@ModelAttribute BoardForm boardForm) {
		boardForm.setfHp(fHp);
		boardForm.setfEp(fEp);
		boardForm.setfMax(fMax);
		boardForm.setfPp(fPp);
		boardForm.setfHand(fHand);
		boardForm.setfSc(fSc);
		boardForm.setfNc(fNc);
		boardForm.setfComb(fComb);
		boardForm.setsHp(sHp);
		boardForm.setsEp(sEp);
		boardForm.setsMax(sMax);
		boardForm.setsPp(sPp);
		boardForm.setsHand(sHand);
		boardForm.setsSc(sSc);
		boardForm.setsNc(sNc);
		boardForm.setsComb(sComb);
		boardForm.setTurn(turn);
		return "board";
	}

	@MessageMapping("/reset")
	public void rest(@Payload String message) {
		fHp = 20;
		fEp = 0;
		fMax = 1;
		fPp = 1;
		fHand = 4;
		fSc = 0;
		fNc = 0;
		fComb = 0;
		
		sHp = 20;
		sEp = 3;
		sMax = 1;
		sPp = 1;
		sHand = 4;
		sSc = 0;
		sNc = 0;
		sComb = 0;
			
		turn = 1;

		String result = "{" +
				"\"fHp\": \"" + fHp + "\"," +
				"\"fEp\": \"" + fEp + "\"," +
				"\"fMax\": \"" + fMax + "\"," +
				"\"fPp\": \"" + fPp + "\"," +
				"\"fHand\": \"" + fHand + "\"," +
				"\"fSc\": \"" + fSc+ "\"," +
				"\"fNc\": \"" + fNc + "\"," +
				"\"fComb\": \"" + fComb + "\"," +
				"\"sHp\": \"" + sHp + "\"," +
				"\"sEp\": \"" + sEp + "\"," +
				"\"sMax\": \"" + sMax + "\"," +
				"\"sPp\": \"" + sPp + "\"," +
				"\"sHand\": \"" + sHand + "\"," +
				"\"sSc\": \"" + sSc+ "\"," +
				"\"sNc\": \"" + sNc + "\"," +
				"\"sComb\": \"" + sComb + "\"," +
				"\"turn\": \"" + turn + "\"" +
				"}";

		template.convertAndSend("/topic/client1", result);
	}
	
	@MessageMapping("/turnChange")
	public void turnChange(@Payload String message) {
		if (turn == 1) {
			// 先行 -> 後攻
			fComb = 0;
			if (fHand > 7) fHand = 7;
			
			if (sMax < 10 && fMax > 1) {
				sMax ++;
				sPp = sMax;
			}
			sHand ++;
			
		} else {
			// 後攻 -> 先行
			sComb = 0;
			if (sHand > 7) sHand = 7;
			
			if (fMax < 10) {
				fMax ++;
				fPp = fMax;
			}
			fHand ++;
		}
		turn *= -1;
		
		String result = "{" +
				"\"fComb\": \"" + fComb + "\"," +
				"\"fHand\": \"" + fHand + "\"," +
				"\"fMax\": \"" + fMax + "\"," +
				"\"fPp\": \"" + fPp + "\"," +
				"\"sComb\": \"" + sComb + "\"," +
				"\"sHand\": \"" + sHand + "\"," +
				"\"sMax\": \"" + sMax + "\"," +
				"\"sPp\": \"" + sPp + "\"," +
				"\"turn\": \"" + turn + "\"" +
				"}";
		
		template.convertAndSend("/topic/client1", result);
	}
	
	@MessageMapping("/fHpMinus")
	public void fHpMinus(@Payload String message) {
		if (fHp > 0) fHp --;
		template.convertAndSend("/topic/client1", "{\"fHp\": \"" + fHp + "\"}");
	}
	@MessageMapping("/fHpPlus")
	public void fHpPlus(@Payload String message) {
		fHp ++;
		template.convertAndSend("/topic/client1", "{\"fHp\": \"" + fHp + "\"}");
	}

	@MessageMapping("/fMaxMinus")
	public void fMaxMinus(@Payload String message) {
		if (fMax > 0) fMax --;
		template.convertAndSend("/topic/client1", "{\"fMax\": \"" + fMax + "\"}");
	}
	@MessageMapping("/fMaxPlus")
	public void fMaxPlus(@Payload String message) {
		if (fMax < 10) fMax ++;
		template.convertAndSend("/topic/client1", "{\"fMax\": \"" + fMax + "\"}");
	}

	@MessageMapping("/fPpMinus")
	public void fPpMinus(@Payload String message) {
		if (fPp > 0) fPp --;
		template.convertAndSend("/topic/client1", "{\"fPp\": \"" + fPp + "\"}");
	}
	@MessageMapping("/fPpPlus")
	public void fPpPlus(@Payload String message) {
		if (fPp < 10) fPp ++;
		template.convertAndSend("/topic/client1", "{\"fPp\": \"" + fPp + "\"}");
	}

	@MessageMapping("/fHandMinus")
	public void fHandMinus(@Payload String message) {
		if (fHand > 0) fHand --;
		template.convertAndSend("/topic/client1", "{\"fHand\": \"" + fHand + "\"}");
	}
	@MessageMapping("/fHandPlus")
	public void fHandPlus(@Payload String message) {
		fHand ++;
		template.convertAndSend("/topic/client1", "{\"fHand\": \"" + fHand + "\"}");
	}

	@MessageMapping("/fScMinus")
	public void fScMinus(@Payload String message) {
		if (fSc > 0) fSc --;
		template.convertAndSend("/topic/client1", "{\"fSc\": \"" + fSc + "\"}");
	}
	@MessageMapping("/fScPlus")
	public void fScPlus(@Payload String message) {
		fSc ++;
		template.convertAndSend("/topic/client1", "{\"fSc\": \"" + fSc + "\"}");
	}
	@MessageMapping("/fNcMinus")
	public void fNcMinus(@Payload String message) {
		if (fNc > 0) fNc --;
		template.convertAndSend("/topic/client1", "{\"fNc\": \"" + fNc + "\"}");
	}
	@MessageMapping("/fNcPlus")
	public void fNcPlus(@Payload String message) {
		fNc ++;
		template.convertAndSend("/topic/client1", "{\"fNc\": \"" + fNc + "\"}");
	}
	@MessageMapping("/fCombMinus")
	public void fCombMinus(@Payload String message) {
		if (fComb > 0) fComb --;
		template.convertAndSend("/topic/client1", "{\"fComb\": \"" + fComb + "\"}");
	}
	@MessageMapping("/fCombPlus")
	public void fCombPlus(@Payload String message) {
		fComb ++;
		template.convertAndSend("/topic/client1", "{\"fComb\": \"" + fComb + "\"}");
	}
	
	///////

	@MessageMapping("/sHpMinus")
	public void sHpMinus(@Payload String message) {
		if (sHp > 0) sHp --;
		template.convertAndSend("/topic/client1", "{\"sHp\": \"" + sHp + "\"}");
	}
	@MessageMapping("/sHpPlus")
	public void sHpPlus(@Payload String message) {
		sHp ++;
		template.convertAndSend("/topic/client1", "{\"sHp\": \"" + sHp + "\"}");
	}

	
	@MessageMapping("/sEpMinus")
	public void sEpMinus(@Payload String message) {
		if (sEp > 0) sEp --;
		template.convertAndSend("/topic/client1", "{\"sEp\": \"" + sEp + "\"}");
	}
	@MessageMapping("/sEpPlus")
	public void sEpPlus(@Payload String message) {
		sEp ++;
		template.convertAndSend("/topic/client1", "{\"sEp\": \"" + sEp + "\"}");
	}
	@MessageMapping("/sMaxMinus")
	public void sMaxMinus(@Payload String message) {
		if (sMax > 0) sMax --;
		template.convertAndSend("/topic/client1", "{\"sMax\": \"" + sMax + "\"}");
	}
	@MessageMapping("/sMaxPlus")
	public void sMaxPlus(@Payload String message) {
		if (sMax < 10) sMax ++;
		template.convertAndSend("/topic/client1", "{\"sMax\": \"" + sMax + "\"}");
	}

	@MessageMapping("/sPpMinus")
	public void sPpMinus(@Payload String message) {
		if (sPp > 0) sPp --;
		template.convertAndSend("/topic/client1", "{\"sPp\": \"" + sPp + "\"}");
	}
	@MessageMapping("/sPpPlus")
	public void sPpPlus(@Payload String message) {
		if (sPp < 10) sPp ++;
		template.convertAndSend("/topic/client1", "{\"sPp\": \"" + sPp + "\"}");
	}

	@MessageMapping("/sHandMinus")
	public void sHandMinus(@Payload String message) {
		if (sHand > 0) sHand --;
		template.convertAndSend("/topic/client1", "{\"sHand\": \"" + sHand + "\"}");
	}
	@MessageMapping("/sHandPlus")
	public void sHandPlus(@Payload String message) {
		sHand ++;
		template.convertAndSend("/topic/client1", "{\"sHand\": \"" + sHand + "\"}");
	}

	@MessageMapping("/sScMinus")
	public void sScMinus(@Payload String message) {
		if (sSc > 0) sSc --;
		template.convertAndSend("/topic/client1", "{\"sSc\": \"" + sSc + "\"}");
	}
	@MessageMapping("/sScPlus")
	public void sScPlus(@Payload String message) {
		sSc ++;
		template.convertAndSend("/topic/client1", "{\"sSc\": \"" + sSc + "\"}");
	}
	@MessageMapping("/sNcMinus")
	public void sNcMinus(@Payload String message) {
		if (sNc > 0) sNc --;
		template.convertAndSend("/topic/client1", "{\"sNc\": \"" + sNc + "\"}");
	}
	@MessageMapping("/sNcPlus")
	public void sNcPlus(@Payload String message) {
		sNc ++;
		template.convertAndSend("/topic/client1", "{\"sNc\": \"" + sNc + "\"}");
	}
	@MessageMapping("/sCombMinus")
	public void sCombMinus(@Payload String message) {
		if (sComb > 0) sComb --;
		template.convertAndSend("/topic/client1", "{\"sComb\": \"" + sComb + "\"}");
	}
	@MessageMapping("/sCombPlus")
	public void sCombPlus(@Payload String message) {
		sComb ++;
		template.convertAndSend("/topic/client1", "{\"sComb\": \"" + sComb + "\"}");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
