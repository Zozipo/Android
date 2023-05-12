using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using WebShop.Abastract;
using WebShop.Constants;
using WebShop.Data.Entitties.Identity;
using WebShop.Helpers;
using WebShop.Models;

namespace WebShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly IJwtTokenService _jwtTokenService;
        private readonly UserManager<UserEntity> _userManager;
        public AccountController(IJwtTokenService jwtTokenService,
           UserManager<UserEntity> userManager)
        {
            _jwtTokenService = jwtTokenService;
            _userManager = userManager;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginViewModel model)
        {

            var user = await _userManager.FindByEmailAsync(model.Email);
            if (user != null)
            {
                var isPasswordValid = await _userManager.CheckPasswordAsync(user, model.Password);
                if (!isPasswordValid)
                {
                    return BadRequest();

                }
                var token = await _jwtTokenService.CreateToken(user);
                return Ok(new { token });
            }
            return BadRequest();
        }
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterUserViewModel model)
        {

            string imageName = String.Empty;

            if (model.Photo != null)
            {
                imageName = ImageWorker.SaveImage(model.Photo);
            }


            UserEntity user = new UserEntity()
            {
                FirstName = model.FirstName,
                LastName = model.LastName,
                UserName = model.Email,
                Email = model.Email,
                Image = imageName
            };

            var result = _userManager.CreateAsync(user, model.Password).Result;
            if (result.Succeeded)
            {
                result = _userManager.AddToRoleAsync(user, Roles.User).Result;
                return Ok();
            }
            else
            {
                return BadRequest();
            }
        }

    }
}
